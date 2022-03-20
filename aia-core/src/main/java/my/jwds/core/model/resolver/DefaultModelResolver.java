package my.jwds.core.model.resolver;

import my.jwds.core.api.definition.FieldDefinition;
import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.model.*;

import java.lang.reflect.*;
import java.util.*;

import static my.jwds.core.model.JavaTypeModelTable.resolveSpecial;

public class DefaultModelResolver implements ModelResolver<Type>{


    private Map<Type,Model> cache = new HashMap<>();
    private DefinitionResolver definitionResolver;



    public DefaultModelResolver(DefinitionResolver definitionResolver) {
        this.definitionResolver = definitionResolver;
    }

    @Override
    public ModelProperty resolve(Type type) {
        ModelPropertyResolveInfo resolveInfo;
        if (type instanceof ModelPropertyResolveInfo){
            resolveInfo = (ModelPropertyResolveInfo) type;
        }else{
            resolveInfo = new ModelPropertyResolveInfo();
            resolveInfo.setType(type);
            resolveInfo.setName("object");
        }
        Type origin = resolveInfo.getOrigin() == null ? resolveInfo.getType():resolveInfo.getOrigin();
        GenericDeclaration declaration = resolveInfo.getDefinition() == null ? getRawClass(resolveInfo.getType()) : resolveInfo.getDefinition();
        return resolveProperty(resolveInfo.getType(),resolveInfo.getName(),origin,declaration,new HashSet());
    }




    protected Model resolveModel(Type type,Set set) {
        if (cache.containsKey(type))return cache.get(type).clone();
        Class clz = getRawClass(type);
        if (set.contains(clz))return new Model();
        set.add(clz);
        Set<String> fieldProcessCache = new HashSet<>();
        List<ModelProperty> properties = new ArrayList<>();
        Map<Field, FieldDefinition> propertyMap = definitionResolver.resolveField(clz);
        for (FieldDefinition fieldDefinition : propertyMap.values()) {
            Field field = fieldDefinition.getField();
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())){
                continue;
            }
            ModelProperty property = resolveProperty(field.getGenericType(),field.getName(),type, field.getDeclaringClass(),set);
            property.setDefinition(fieldDefinition.getDefinition());
            properties.add(property);
            fieldProcessCache.add(field.getName());
        }
        set.remove(clz);
        Model model = new Model(properties);
        cache.put(type,model.clone());
        return model;
    }

    protected ModelProperty resolveProperty(Type type,String name,Type origin,GenericDeclaration definition,Set set){
        ModelProperty res = null;
        if (type instanceof Class){
            res = resolveClass((Class) type,name,origin,set);
        }else if (type instanceof TypeVariable){
            res = resolveTypeVariable((TypeVariable) type, name, origin, definition,set);
        }else if (type instanceof ParameterizedType){
            res = resolveParameterType((ParameterizedType) type, name, origin, definition,set);
        }else if (type instanceof GenericArrayType){
            res = resolveGenericArrayType((GenericArrayType) type, name, origin, definition,set);
        }else if (type instanceof WildcardType){
            res = resolveWildcardType((WildcardType) type, name, origin, definition,set);
        }
        if (res instanceof ArrayModelProperty){
            res.setType(((ArrayModelProperty) res).getComponent().getType()+"[]");
        }
        return res;
    }



    protected ModelProperty resolveClass(Class clz,String name,Type origin,Set set){
        ModelProperty now  = resolveSpecial(clz,clz,name);
        if (now instanceof ObjectModelProperty){
            ((ObjectModelProperty) now).setModel(resolveModel(clz,set));
        }else if (now instanceof ArrayModelProperty){
            ((ArrayModelProperty) now).setComponent(resolveProperty(clz.getComponentType(),null,origin,null,set));
        }
        return now;
    }
    protected ModelProperty resolveTypeVariable(TypeVariable type,String name,Type origin,GenericDeclaration definition,Set set){
        ModelProperty res = null;
        Type[] actual = getActualSuperType(type, origin, definition);
        if (actual ==null){
            if (type.getBounds().length != 0) res = resolveProperty(type.getBounds()[0],name,origin,definition,set);
            else res = resolveProperty(Object.class,name,origin,definition,set);
        }else{
            TypeVariable[] definitionTypes = definition.getTypeParameters();
            for (int i = 0; i < definitionTypes.length; i++) {
                if (definitionTypes[i] == type){
                    res = resolveProperty(actual[i],name,origin,definition,set);
                    break;
                }
            }
        }
        return res;
    }

    private Type[] getActualSuperType(TypeVariable type,Type origin,GenericDeclaration definition){
        Type[] actual = origin instanceof ParameterizedType? ((ParameterizedType) origin).getActualTypeArguments() : null;
        if(type.getGenericDeclaration() == definition)return actual;
        Class now;
        Type [] prev;
        while((now = getRawClass(origin)) != definition){
            origin = getGenericSuperClass(origin);
            if (origin instanceof ParameterizedType){
                prev = actual;
                actual = ((ParameterizedType) origin).getActualTypeArguments();
                if (prev != null)continue;
                for (int i = 0; i < actual.length; i++) {
                    for (int z = 0; z < prev.length; z++) {
                        if (actual[i] == now.getTypeParameters()[z]){
                            actual[i] = prev[z];
                            break;
                        }
                    }
                }
            }else{
                actual = null;
            }
        }
        return actual;
    }

    private Type getGenericSuperClass(Type type){
        return type instanceof Class ? ((Class)type).getGenericSuperclass(): ((Class)((ParameterizedType)type).getRawType()).getGenericSuperclass();
    }
    private Class getRawClass(Type type){
        return type instanceof Class?(Class)type: (Class) ((ParameterizedType)type).getRawType();
    }

    protected ModelProperty resolveParameterType(ParameterizedType type,String name,Type origin,GenericDeclaration definition,Set set){
        Class clz = (Class) type.getRawType();
        ModelProperty now  = resolveSpecial(clz,type,name);
        if (now instanceof ObjectModelProperty){
            if (now.getJavaType() == Map.class){
                ModelProperty[] properties = new ModelProperty[type.getActualTypeArguments().length];
                for (int i = 0; i < type.getActualTypeArguments().length; i++) {
                    properties[i] = resolveProperty(type.getActualTypeArguments()[i],null,origin,definition,set);
                }
                ((ObjectModelProperty) now).setContainerContent(properties);
            }else{
                ((ObjectModelProperty) now).setModel(resolveModel(type,set));
            }
        }else if (now instanceof ArrayModelProperty){
            ((ArrayModelProperty) now).setComponent(resolveProperty(type.getActualTypeArguments()[0],null,origin,definition,set));
        }
        return now;
    }

    protected ModelProperty resolveGenericArrayType(GenericArrayType type,String name,Type origin,GenericDeclaration definition,Set set){
        return new ArrayModelProperty(name,null,resolveProperty(type.getGenericComponentType(),null,origin,definition,set));
    }

    protected ModelProperty resolveWildcardType(WildcardType type, String name, Type origin, GenericDeclaration definition,Set set){
        return resolveProperty(type.getUpperBounds()[0],name,origin,definition,set);
    }










}
