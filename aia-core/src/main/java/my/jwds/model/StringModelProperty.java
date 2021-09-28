package my.jwds.model;

public class StringModelProperty extends ModelProperty{

    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringModelProperty(String name, String type, String value) {
        super(name, type);
        this.value = value;
    }

    public StringModelProperty() {

    }

    public StringModelProperty clone(){
        return new StringModelProperty(getName(),getType(),getValue());
    }
}