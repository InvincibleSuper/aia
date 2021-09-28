package my.jwds.definition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ClassDefinition {

    private Class clz;


    private Map<Method,MethodDefinition> methodDefinitions;


    private Map<Field,PropertyDefinition>  propertyDefinitions;


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

    public Map<Field, PropertyDefinition> getPropertyDefinitions() {
        return propertyDefinitions;
    }

    public void setPropertyDefinitions(Map<Field, PropertyDefinition> propertyDefinitions) {
        this.propertyDefinitions = propertyDefinitions;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public ClassDefinition(Class clz, Map<Method, MethodDefinition> methodDefinitions, Map<Field, PropertyDefinition> propertyDefinitions, String definition) {
        this.clz = clz;
        this.methodDefinitions = methodDefinitions;
        this.propertyDefinitions = propertyDefinitions;
        this.definition = definition;
    }

    public ClassDefinition() {
    }
}
