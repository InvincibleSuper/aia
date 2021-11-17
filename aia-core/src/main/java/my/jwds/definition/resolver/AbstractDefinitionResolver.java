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


    /**
     * 初始化注释
     * @param clz 解析类
     */
    protected void initDefinition(Class clz){
        if (cache.get(clz) == null)doInitDefinition(clz);
    }

    protected abstract void doInitDefinition(Class clz);


    /**
     * 解析类注释
     * @param clz
     * @return
     */
    @Override
    public ClassDefinition resolveClass(Class clz) {
        initDefinition(clz);
        return cache.get(clz);
    }

    /**
     * 解析属性注释
     * @param clz 解析的类
     * @return 属性和属性注释的键值对
     */
    @Override
    public Map<Field, PropertyDefinition> resolveProperty(Class clz) {
        initDefinition(clz);
        return resolveClass(clz).getPropertyDefinitions();
    }

    /**
     * 解析方法注释
     * @param clz 解析类
     * @return 方法和方法注释的键值对
     */
    @Override
    public Map<Method, MethodDefinition> resolveMethod(Class clz) {
        initDefinition(clz);
        return resolveClass(clz).getMethodDefinitions();
    }

    /**
     * 返回所有已解析注释
     * @return 类和类注释的键值对
     */
    @Override
    public Map<Class, ClassDefinition> getCache() {
        return cache;
    }


}
