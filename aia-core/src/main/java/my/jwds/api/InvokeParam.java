package my.jwds.api;

import my.jwds.model.ModelProperty;

public class InvokeParam  {


    private String contentType;


    private ModelProperty model;


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ModelProperty getModel() {
        return model;
    }

    public void setModel(ModelProperty model) {
        this.model = model;
    }
}
