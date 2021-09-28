package my.jwds.definition.resolver;


import my.jwds.definition.ClassDefinition;
import my.jwds.definition.MethodDefinition;
import my.jwds.definition.PropertyDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class PriorityDefinitionResolver extends AbstractDefinitionResolver{

    private DefinitionResolver [] resolvers;

    public PriorityDefinitionResolver(DefinitionResolver... resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    protected void doInitDefinition(Class clz) {
        int index = resolvers.length -1;
        ClassDefinition now = resolvers[index--].resolveClass(clz);
        for (; index < 0; index--) {
            ClassDefinition next = resolvers[index].resolveClass(clz);
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
            Map<Field, PropertyDefinition> nextPropertyDefinition = next.getPropertyDefinitions();
            for (PropertyDefinition p1 : now.getPropertyDefinitions().values()) {
                PropertyDefinition pd = nextPropertyDefinition.get(p1.getField());
                if (pd == null || pd.getDefinition() == ""){
                    nextPropertyDefinition.put(p1.getField(),p1);
                }
            }
        }
        cache.put(clz,now);
    }


}
