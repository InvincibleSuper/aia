package my.jwds.core.template;

import my.jwds.core.api.InvokeApi;
import my.jwds.core.api.InvokeUrl;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模板，在aia里是一个调用的总集成，包含api，返回值
 * 本身是一个调用链，next指向下一个模板
 */
public class AiaTemplate {


    /**
     * 名称
     */
    private String name;

    /**
     * 组
     */
    private String group;

    /**
     * 调用url
     */
    private InvokeUrl url;

    /**
     * 参数
     */
    private Map<String,TemplateParam> params;


    /**
     * 请求头
     */
    private Map<String,String> headers;

    /**
     * 返回值值处理器
     */
    private ReturnValueProcessor processor;

    /**
     * 下一个模板
     */
    private AiaTemplate next;

    /**
     * 是否是返回值处理器成功后执行
     */
    private boolean successNext;

    /**
     * 返回值提供下一个模板的请求参数
     */
    private Map<String,String> returnAsParameter;

    /**
     * 传入值对应插件
     */
    private Map<String,String> plugins;



    class TemplateParam{
        String type;
        String value;
        boolean array;

        public TemplateParam(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public TemplateParam(String type, String value, boolean array) {
            this.type = type;
            this.value = value;
            this.array = array;
        }
    }


    public void addParam(String name,String value,String type){
        if (params == null) {
            params = new LinkedHashMap<>();
            params.put(name,new TemplateParam(type,value));
        }
    }

    public void addParam(String name,String value,String type,boolean array){
        if (params == null) {
            params = new LinkedHashMap<>();
            params.put(name,new TemplateParam(type,value,array));
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public InvokeUrl getUrl() {
        return url;
    }

    public void setUrl(InvokeUrl url) {
        this.url = url;
    }

    public ReturnValueProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(ReturnValueProcessor processor) {
        this.processor = processor;
    }

    public AiaTemplate getNext() {
        return next;
    }

    public void setNext(AiaTemplate next) {
        this.next = next;
    }

    public boolean isSuccessNext() {
        return successNext;
    }

    public void setSuccessNext(boolean successNext) {
        this.successNext = successNext;
    }

    public Map<String, String> getReturnAsParameter() {
        return returnAsParameter;
    }

    public void setReturnAsParameter(Map<String, String> returnAsParameter) {
        this.returnAsParameter = returnAsParameter;
    }

    public Map<String, String> getPlugins() {
        return plugins;
    }

    public void setPlugins(Map<String, String> plugins) {
        this.plugins = plugins;
    }

    public Map<String, TemplateParam> getParams() {
        return params;
    }

    public void setParams(Map<String, TemplateParam> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
