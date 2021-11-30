package my.jwds.core.template;

import my.jwds.core.api.InvokeApi;

import java.util.Map;

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
     * 调用api
     */
    private InvokeApi api;

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

    public InvokeApi getApi() {
        return api;
    }

    public void setApi(InvokeApi api) {
        this.api = api;
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
}
