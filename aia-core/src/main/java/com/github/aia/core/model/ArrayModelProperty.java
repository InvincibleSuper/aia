package com.github.aia.core.model;



public class ArrayModelProperty extends ModelProperty{

    private ModelProperty component;



    public ArrayModelProperty() {
    }
    public ArrayModelProperty(String name,Class javaType) {
        super(name,javaType);
    }
    public ArrayModelProperty(String name,Class javaType, ModelProperty component) {
        super(name,javaType);
        this.component = component;
    }

    public ArrayModelProperty(String name, String type, Class javaType) {
        super(name, type, javaType);
    }

    public ArrayModelProperty clone(){
        ArrayModelProperty arrayModelProperty = new ArrayModelProperty();
        arrayModelProperty.setName(getName());
        arrayModelProperty.setType(getType());
        arrayModelProperty.setJavaType(getJavaType());
        arrayModelProperty.setDefinition(getDefinition());

        if (getComponent() != null){
            arrayModelProperty.setComponent(getComponent().clone());
        }
        return arrayModelProperty;
    }

    public ModelProperty getComponent() {
        return component;
    }

    public void setComponent(ModelProperty component) {
        this.component = component;
    }
}
