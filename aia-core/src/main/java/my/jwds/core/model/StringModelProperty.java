package my.jwds.core.model;

public class StringModelProperty extends SimpleModelProperty{

    private String value;





    public StringModelProperty() {

    }
    public StringModelProperty(String name,Class javaType, String value) {
        super(name,javaType);
        this.value = value;
    }

    public StringModelProperty(String name, String type, Class javaType, String value) {
        super(name, type, javaType);
        this.value = value;
    }

    public StringModelProperty clone(){
        StringModelProperty modelProperty = new StringModelProperty();
        modelProperty.setName(getName());
        modelProperty.setType(getType());
        modelProperty.setJavaType(getJavaType());
        modelProperty.setDefinition(getDefinition());
        modelProperty.setValue(getValue());
        return modelProperty;
    }




    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
