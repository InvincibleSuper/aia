package my.jwds.api;

import my.jwds.model.ModelProperty;

import java.util.Map;

public class InvokeApi extends InvokeDefinition{

    private InvokeUrl url;


    private Map<String,InvokeParam> params;


    private Map<String,String> headers;


    private String group;


    private ModelProperty returnModel;


    private String returnType;

    public ModelProperty getReturnModel() {
        return returnModel;
    }

    public void setReturnModel(ModelProperty returnModel) {
        this.returnModel = returnModel;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getGroup() {
        return group;
    }

    public InvokeApi setGroup(String group) {
        this.group = group;
        return this;
    }

    public InvokeUrl getUrl() {
        return url;
    }

    public InvokeApi setUrl(InvokeUrl url) {
        this.url = url;
        return this;
    }

    public Map<String, InvokeParam> getParams() {
        return params;
    }

    public InvokeApi setParams(Map<String, InvokeParam> params) {
        this.params = params;
        return this;
    }


}
