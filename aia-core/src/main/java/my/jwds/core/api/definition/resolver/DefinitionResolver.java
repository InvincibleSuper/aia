package my.jwds.core.api.definition.resolver;

import my.jwds.core.api.definition.ClassDefinition;
import my.jwds.core.api.definition.MethodDefinition;
import my.jwds.core.api.definition.FieldDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public interface DefinitionResolver {

    /**
     * 解析类注释
     * @param clz 类
     * @return 类定义
     */
    ClassDefinition resolveClass(Class clz);

    /**
     * 解析属性注释
     * @param clz 解析的类
     * @return 属性和属性注释的键值对
     */
    Map<Field, FieldDefinition> resolveField(Class clz);

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


    /**
     * 解析属性注释
     * @param clz 解析的类
     * @return 属性和属性注释的键值对
     */
    FieldDefinition resolveField(Class clz,Field field);

    /**
     * 解析方法注释
     * @param clz 解析类
     * @return 方法和方法注释的键值对
     */
    MethodDefinition resolveMethod(Class clz,Method method);

    /**
     * 返回类注释
     * @param clz 类
     * @return 类注释
     */
    String resolveClassDefinition(Class clz);

    /**
     * 返回字段注释
     * @param field 字段
     * @return 字段注释
     */
    String resolveFieldDefinition(Field field);

    /**
     * 返回方法注释
     * @param method 方法
     * @return 方法注释
     */
    String resolveMethodDefinition(Method method);


    /**
     * 返回方法参数注释
     * @param method 方法
     * @return 方法参数注释
     */
    String[] resolveMethodPropertiesDefinition(Method method);


    /**
     * 返回方法参数注释
     * @param method 方法
     * @param parameter 方法参数
     * @return 方法参数注释
     */
    String resolveMethodPropertiesDefinition(Method method, Parameter parameter);
}
