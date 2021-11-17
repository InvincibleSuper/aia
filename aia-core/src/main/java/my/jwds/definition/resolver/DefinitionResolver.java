package my.jwds.definition.resolver;

import my.jwds.definition.ClassDefinition;
import my.jwds.definition.MethodDefinition;
import my.jwds.definition.PropertyDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public interface DefinitionResolver {

    /**
     * 解析类注释
     * @param clz
     * @return
     */
    ClassDefinition resolveClass(Class clz);

    /**
     * 解析属性注释
     * @param clz 解析的类
     * @return 属性和属性注释的键值对
     */
    Map<Field,PropertyDefinition> resolveProperty(Class clz);

    /**
     * 解析方法注释
     * @param clz 解析类
     * @return 方法和方法注释的键值对
     */
    Map<Method,MethodDefinition> resolveMethod(Class clz);

    /**
     * 返回所有已解析注释
     * @return 类和类注释的键值对
     */
    Map<Class,ClassDefinition> getCache();
}
