package my.jwds.core.template;

import my.jwds.core.model.ModelProperty;


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
