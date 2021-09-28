package my.jwds.model;

public class NumberModelProperty extends ModelProperty{

    private Number value;


    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public NumberModelProperty(String name, String type, Number value) {
        super(name, type);
        this.value = value;
    }

    public NumberModelProperty() {

    }
    public NumberModelProperty clone(){
        return new NumberModelProperty(getName(),getType(),getValue());
    }


}