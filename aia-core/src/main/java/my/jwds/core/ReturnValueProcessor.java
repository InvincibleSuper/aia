package my.jwds.core;

import my.jwds.model.ModelProperty;


public class ReturnValueProcessor {


    private ModelProperty model;


    private String returnType;

    public ModelProperty getModel() {
        return model;
    }

    public void setModel(ModelProperty model) {
        this.model = model;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
