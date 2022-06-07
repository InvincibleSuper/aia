package com.github.aia.core.api.definition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ClassDefinition {

    private Class clz;


    private Map<Method,MethodDefinition> methodDefinitions;


    private Map<Field, FieldDefinition>  fieldDefinitions;


    private String definition;


    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public Map<Method, MethodDefinition> getMethodDefinitions() {
        return methodDefinitions;
    }

    public void setMethodDefinitions(Map<Method, MethodDefinition> methodDefinitions) {
        this.methodDefinitions = methodDefinitions;
    }

    public Map<Field, FieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    public void setFieldDefinitions(Map<Field, FieldDefinition> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
    }

    public ClassDefinition(Class clz, Map<Method, MethodDefinition> methodDefinitions, Map<Field, FieldDefinition> fieldDefinitions, String definition) {
        this.clz = clz;
        this.methodDefinitions = methodDefinitions;
        this.fieldDefinitions = fieldDefinitions;
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public ClassDefinition() {
    }
}
