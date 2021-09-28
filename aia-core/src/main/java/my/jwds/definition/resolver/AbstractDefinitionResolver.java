package my.jwds.definition.resolver;

import my.jwds.definition.AiaDefinitionException;
import my.jwds.definition.ClassDefinition;
import my.jwds.definition.MethodDefinition;
import my.jwds.definition.PropertyDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDefinitionResolver implements DefinitionResolver{

    protected HashMap<Class,ClassDefinition> cache = new HashMap<>();

    public AbstractDefinitionResolver(){

    }

    protected void initDefinition(Class clz){
        if (cache.get(clz) == null)doInitDefinition(clz);
    }

    protected abstract void doInitDefinition(Class clz);

    @Override
    public ClassDefinition resolveClass(Class clz) {
        initDefinition(clz);
        return cache.get(clz);
    }


    @Override
    public Map<Field,PropertyDefinition> resolveProperty(Class clz) {
        initDefinition(clz);
        return resolveClass(clz).getPropertyDefinitions();
    }

    @Override
    public Map<Method,MethodDefinition> resolveMethod(Class clz) {
        initDefinition(clz);
        return resolveClass(clz).getMethodDefinitions();
    }

    @Override
    public Map<Class, ClassDefinition> getCache() {
        return cache;
    }
}
