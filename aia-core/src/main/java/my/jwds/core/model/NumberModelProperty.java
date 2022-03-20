package my.jwds.core.model;

public class NumberModelProperty extends SimpleModelProperty{

    private Number value;


    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }



    public NumberModelProperty() {

    }

    public NumberModelProperty(String name,Class javaType, Number value) {
        super(name,javaType);
        this.value = value;
    }


    public NumberModelProperty(String name, String type, Class javaType, Number value) {
        super(name, type, javaType);
        this.value = value;
    }

    public NumberModelProperty clone(){
        NumberModelProperty modelProperty = new NumberModelProperty();
        modelProperty.setName(getName());
        modelProperty.setType(getType());
        modelProperty.setJavaType(getJavaType());
        modelProperty.setDefinition(getDefinition());
        modelProperty.setValue(getValue());
        return modelProperty;
    }


}
