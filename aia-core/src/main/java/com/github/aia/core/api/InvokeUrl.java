package com.github.aia.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 执行url，包含 请求方法和请求路径
 */
public class InvokeUrl extends InvokeDefinition {

    private String method;

    private String url;

    private List<String> accessUrls;


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

    public List<String> getAccessUrls() {
        return accessUrls;
    }

    public void setAccessUrls(List<String> accessUrls) {
        this.accessUrls = accessUrls;
    }

    public InvokeUrl() {
    }

    public InvokeUrl(String definition, String method, String url, List<String> accessUrls) {
        super(definition);
        this.method = method;
        this.url = url;
        this.accessUrls = accessUrls;
    }

    public InvokeUrl clone(){
        List<String> cloneHosts = null;
        if (getAccessUrls() != null)cloneHosts = new ArrayList<>(getAccessUrls());
        return new InvokeUrl(getDefinition(),getMethod(),getUrl(),cloneHosts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvokeUrl invokeUrl = (InvokeUrl) o;
        return Objects.equals(method, invokeUrl.method) && Objects.equals(url, invokeUrl.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, url);
    }
}
