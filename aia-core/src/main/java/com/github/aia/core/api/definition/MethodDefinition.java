package com.github.aia.core.api.definition;

import java.lang.reflect.Method;

public class MethodDefinition {

    private Method method;


    private String definition;


    private String returnDefinition;


    private String [] parameterDefinition;


    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getReturnDefinition() {
        return returnDefinition;
    }

    public void setReturnDefinition(String returnDefinition) {
        this.returnDefinition = returnDefinition;
    }

    public String[] getParameterDefinition() {
        return parameterDefinition;
    }

    public void setParameterDefinition(String[] parameterDefinition) {
        this.parameterDefinition = parameterDefinition;
    }

    public MethodDefinition(Method method, String definition) {
        this.method = method;
        this.definition = definition;
    }

    public MethodDefinition(Method method, String definition, String returnDefinition, String[] parameterDefinition) {
        this.method = method;
        this.definition = definition;
        this.returnDefinition = returnDefinition;
        this.parameterDefinition = parameterDefinition;
    }

    public MethodDefinition() {
    }
}
