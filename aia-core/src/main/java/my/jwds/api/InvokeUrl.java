package my.jwds.api;

import my.jwds.model.ModelProperty;

public class InvokeUrl extends InvokeDefinition {

    private String method;

    private String url;




    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
