package my.jwds.core.api.definition.resolver;


import my.jwds.core.api.definition.ClassDefinition;
import my.jwds.core.api.definition.MethodDefinition;
import my.jwds.core.api.definition.FieldDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PriorityDefinitionResolver extends AbstractDefinitionResolver{

    private List<DefinitionResolver> resolvers = new ArrayList<>();


    public PriorityDefinitionResolver() {
    }

    public PriorityDefinitionResolver(List<DefinitionResolver> resolvers) {
        for (DefinitionResolver resolver : resolvers) {
            addResolver(resolver);
        }
    }

    public void addResolver(DefinitionResolver resolver){
        if (resolver instanceof PriorityDefinitionResolver){
            resolvers.addAll(((PriorityDefinitionResolver)resolvers).getResolvers());
        }else{
            resolvers.add(resolver);
        }
    }

    public List<DefinitionResolver> getResolvers(){
        return resolvers;
    }

    @Override
    protected void doInitDefinition(Class clz) {
        if (resolvers.isEmpty()){
            return;
        }
        int index = resolvers.size() -1;
        ClassDefinition now = resolvers.get(index--).resolveClass(clz);
        for (; index >= 0; index--) {
            ClassDefinition next = resolvers.get(index--).resolveClass(clz);
            if (next.getDefinition() == ""){
                next.setDefinition(now.getDefinition());
            }
            Map<Method,MethodDefinition> nextMethodDefinition = next.getMethodDefinitions();
            for (MethodDefinition m1 : now.getMethodDefinitions().values()) {
                MethodDefinition md = nextMethodDefinition.get(m1.getMethod());
                if (md == null){
                    nextMethodDefinition.put(m1.getMethod(),m1);
                }else{
                    if (md.getReturnDefinition() == null){
                        md.setReturnDefinition(m1.getReturnDefinition());
                    }
                    for (int i = 0; i < m1.getParameterDefinition().length; i++) {
                        if (md.getParameterDefinition()[i] == ""){
                            md.getParameterDefinition()[i] = m1.getParameterDefinition()[i];
                        }
                    }
                }
            }
            Map<Field, FieldDefinition> nextPropertyDefinition = next.getFieldDefinitions();
            for (FieldDefinition p1 : now.getFieldDefinitions().values()) {
                FieldDefinition pd = nextPropertyDefinition.get(p1.getField());
                if (pd == null || pd.getDefinition() == ""){
                    nextPropertyDefinition.put(p1.getField(),p1);
                }
            }
        }
        cache.put(clz,now);
    }


}
