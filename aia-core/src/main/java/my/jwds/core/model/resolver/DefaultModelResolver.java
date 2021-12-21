package my.jwds.core.model.resolver;

import my.jwds.core.api.definition.FieldDefinition;
import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.model.*;

import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DefaultModelResolver implements ModelResolver<Type>{

    private static final Map<Class,Object> special = new HashMap<>();
    private static final ObjectModelProperty OBJECT_MODEL_PROPERTY = new ObjectModelProperty("object",Object.class.getTypeName(),null,new Model());
    private Map<Type,Model> cache = new HashMap<>();
    private DefinitionResolver definitionResolver;

    static{
        Date NOW = new Date();
        String NOW_TIME = new SimpleDateFormat("hh:mm:ss").format(NOW);
        String NOW_DATE = new SimpleDateFormat("yyyy-MM-dd").format(NOW);
        String NOW_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(NOW);
        special.put(byte.class,new NumberModelProperty(null,byte.class.getTypeName(),0));
        special.put(char.class,new StringModelProperty(null,char.class.getTypeName(),"A"));
        special.put(boolean.class,new StringModelProperty(null,boolean.class.getTypeName(),"false"));
        special.put(short.class,new NumberModelProperty(null,short.class.getTypeName(),0));
        special.put(int.class,new NumberModelProperty(null,int.class.getTypeName(),0));
        special.put(float.class,new NumberModelProperty(null,float.class.getTypeName(),1.0));
        special.put(double.class,new NumberModelProperty(null,double.class.getTypeName(),1.00 ));
        special.put(long.class,new NumberModelProperty(null,long.class.getTypeName(),0 ));
        special.put(String.class,new StringModelProperty(null,String.class.getTypeName(),"String" ));
        special.put(Character.class,new StringModelProperty(null,Character.class.getTypeName(),"A" ));
        special.put(Boolean.class,new StringModelProperty(null,Boolean.class.getTypeName(),"false" ));
        LinkedHashMap<Class,Object> number = new LinkedHashMap<>();
        number.put(Byte.class,new NumberModelProperty(null,Byte.class.getTypeName(),0 ));
        number.put(Short.class,new NumberModelProperty(null,Short.class.getTypeName(),0 ));
        number.put(Integer.class,new NumberModelProperty(null,Integer.class.getTypeName(),0));
        number.put(Float.class,new NumberModelProperty(null,Float.class.getTypeName(),0.0 ));
        number.put(Double.class,new NumberModelProperty(null,Double.class.getTypeName(),0.00 ));
        number.put(Long.class,new NumberModelProperty(null,Long.class.getTypeName(),0 ));
        number.put(Number.class,new NumberModelProperty(null,Number.class.getTypeName(),0 ));
        special.put(Number.class,number);
        special.put(Date.class,new StringModelProperty(null,Date.class.getTypeName(), NOW_DATE_TIME ));
        special.put(LocalDate.class,new StringModelProperty(null,LocalDate.class.getTypeName(), NOW_DATE ));
        special.put(LocalTime.class,new StringModelProperty(null,LocalTime.class.getTypeName(), NOW_TIME ));
        special.put(LocalDateTime.class,new StringModelProperty(null,LocalDateTime.class.getTypeName(), NOW_DATE_TIME ));
        ModelProperty key = ((ModelProperty) special.get(String.class)).clone();
        key.setName("key");
        ModelProperty value = OBJECT_MODEL_PROPERTY.clone();
        value.setName("value");
        special.put(Map.class,new ObjectModelProperty(null,Map.class.getTypeName(),new ModelProperty[]{key,value},null));
        special.put(Collection.class,new ArrayModelProperty(null,Collection.class.getTypeName()));
    }

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
        return res;
    }



    protected ModelProperty resolveClass(Class clz,String name,Type origin,Set set){
        ModelProperty now  = resolveSpecial(clz,clz,name);
        if (now == null){
            now = new ObjectModelProperty(name,clz.getTypeName(),null,resolveModel(clz,set));
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
        if (now == null){
            now = new ObjectModelProperty(name,clz.getTypeName(),null,resolveModel(type,set));
        }else if (now instanceof ObjectModelProperty){
            ModelProperty[] properties = new ModelProperty[type.getActualTypeArguments().length];
            for (int i = 0; i < type.getActualTypeArguments().length; i++) {
                properties[i] = resolveProperty(type.getActualTypeArguments()[i],null,origin,definition,set);
            }
            ((ObjectModelProperty) now).setContainerContent(properties);
        }else if (now instanceof ArrayModelProperty){
            ((ArrayModelProperty) now).setComponent(resolveProperty(type.getActualTypeArguments()[0],null,origin,definition,set));
        }
        return now;
    }

    protected ModelProperty resolveGenericArrayType(GenericArrayType type,String name,Type origin,GenericDeclaration definition,Set set){
        return new ArrayModelProperty(name,type.getTypeName(),resolveProperty(type.getGenericComponentType(),null,origin,definition,set));
    }

    protected ModelProperty resolveWildcardType(WildcardType type, String name, Type origin, GenericDeclaration definition,Set set){
        return resolveProperty(type.getUpperBounds()[0],name,origin,definition,set);
    }





    protected ModelProperty resolveSpecial(Class clz,Type type,String name){
        if (clz.isArray()){
            return new ArrayModelProperty(name,type.toString());
        }else if (clz == Object.class){
            ObjectModelProperty objectModelProperty = OBJECT_MODEL_PROPERTY.clone();
            objectModelProperty.setName(name);
            return objectModelProperty;
        }
        Object o = special;
        while(o != null){
            if (o instanceof Map){
                Map<Class,Object> searchMap = (Map<Class, Object>) o;
                o = searchMap.get(clz);
                if (o == null){
                    for (Class aClass : searchMap.keySet()) {
                        if (aClass.isAssignableFrom(clz)){
                            o = searchMap.get(aClass);
                            break;
                        }
                    }
                }
            }else{
                ModelProperty template =  ((ModelProperty)o);
                ModelProperty res = template.clone();
                res.setName(name);
                res.setType(type.toString());
                return res;
            }

        }

        return null;
    }




}
