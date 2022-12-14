package com.github.aia.core.model;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;

public class ModelPropertyResolveInfo implements Type{
    private Type type;
    private String name;
    private Type origin;
    private GenericDeclaration definition;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getOrigin() {
        return origin;
    }

    public void setOrigin(Type origin) {
        this.origin = origin;
    }

    public GenericDeclaration getDefinition() {
        return definition;
    }

    public void setDefinition(GenericDeclaration definition) {
        this.definition = definition;
    }



    public ModelPropertyResolveInfo(Type type, String name, Type origin, GenericDeclaration definition) {
        this.type = type;
        this.name = name;
        this.origin = origin;
        this.definition = definition;
    }

    public ModelPropertyResolveInfo(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public ModelPropertyResolveInfo() {
    }
}
