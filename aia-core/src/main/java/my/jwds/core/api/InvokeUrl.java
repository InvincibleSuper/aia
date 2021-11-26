package my.jwds.core.api;

/**
 * 执行url，包含 请求方法和请求路径
 */
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

    public InvokeUrl(String definition, String method, String url) {
        super(definition);
        this.method = method;
        this.url = url;
    }

    public InvokeUrl() {
    }
}
