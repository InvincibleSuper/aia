package my.jwds.api.definition;

import java.lang.reflect.Field;

public class FieldDefinition {


    private Field field;


    private String definition;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public FieldDefinition(Field field, String definition) {
        this.field = field;
        this.definition = definition;
    }

    public FieldDefinition() {
    }
}
