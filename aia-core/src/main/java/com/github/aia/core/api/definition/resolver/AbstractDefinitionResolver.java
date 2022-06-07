package com.github.aia.core.api.definition.resolver;

import com.github.aia.core.api.definition.ClassDefinition;
import com.github.aia.core.api.definition.MethodDefinition;
import com.github.aia.core.api.definition.FieldDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        if (cache.get(clz) == null){
            Map<Field,FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();
            Map<Method,MethodDefinition> methodDefinitionMap = new LinkedHashMap<>();
            Class now = clz;
            while(now!=null){
                for (Field field : clz.getDeclaredFields()) {
                    fieldDefinitionMap.put(field,new FieldDefinition(field,null));
                }
                for (Method method : clz.getDeclaredMethods()) {
                    methodDefinitionMap.put(method,new MethodDefinition(method,null,null,new String[method.getParameterCount()]));
                }
                now = now.getSuperclass();
            }
            cache.put(clz,new ClassDefinition(clz,methodDefinitionMap,fieldDefinitionMap,null));
        }
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
    public Map<Field, FieldDefinition> resolveField(Class clz) {
        return resolveClass(clz).getFieldDefinitions();
    }

    /**
     * 解析方法注释
     * @param clz 解析类
     * @return 方法和方法注释的键值对
     */
    @Override
    public Map<Method, MethodDefinition> resolveMethod(Class clz) {
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

    /**
     * 解析属性注释
     *
     * @param clz   解析的类
     * @param field
     * @return 属性和属性注释的键值对
     */
    @Override
    public FieldDefinition resolveField(Class clz, Field field) {
        return resolveField(clz).get(field);
    }

    /**
     * 解析方法注释
     *
     * @param clz    解析类
     * @param method
     * @return 方法和方法注释的键值对
     */
    @Override
    public MethodDefinition resolveMethod(Class clz, Method method) {
        return resolveMethod(clz).get(method);
    }

    /**
     * 返回类注释
     *
     * @param clz 类
     * @return 类注释
     */
    @Override
    public String resolveClassDefinition(Class clz) {
        return resolveClass(clz).getDefinition();
    }

    /**
     * 返回字段注释
     *
     * @param field 字段
     * @return 字段注释
     */
    @Override
    public String resolveFieldDefinition(Field field) {
        FieldDefinition fieldDefinition = resolveClass(field.getDeclaringClass()).getFieldDefinitions().get(field);
        return fieldDefinition == null ? null : fieldDefinition.getDefinition();
    }

    /**
     * 返回方法注释
     *
     * @param method 方法
     * @return 方法注释
     */
    @Override
    public String resolveMethodDefinition( Method method) {
        MethodDefinition methodDefinition = resolveClass(method.getDeclaringClass()).getMethodDefinitions().get(method);
        return methodDefinition == null ? null:methodDefinition.getDefinition();
    }

    /**
     * 返回方法参数注释
     *
     * @param method 方法

     * @return 方法参数注释
     */
    @Override
    public String[] resolveMethodPropertiesDefinition(Method method) {
        MethodDefinition methodDefinition = resolveClass(method.getDeclaringClass()).getMethodDefinitions().get(method);
        return methodDefinition == null ? new String[method.getParameterCount()]:methodDefinition.getParameterDefinition();
    }

    /**
     * 返回方法参数注释
     *
     * @param method    方法
     * @param parameter 方法参数
     * @return 方法参数注释
     */
    @Override
    public String resolveMethodPropertiesDefinition(Method method, Parameter parameter) {
        String[] parameterDefinitions = resolveMethodPropertiesDefinition(method);
        for (int i = 0; i < method.getParameters().length; i++) {
            if (parameter == method.getParameters()[i])return parameterDefinitions[i];
        }
        return null;
    }
}
