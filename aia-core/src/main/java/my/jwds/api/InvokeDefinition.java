package my.jwds.api;

import java.io.Serializable;

public class InvokeDefinition implements Serializable {

    private String definition;


    public String getDefinition() {
        return definition;
    }

    public InvokeDefinition setDefinition(String definition) {
        this.definition = definition;
        return this;
    }
}
