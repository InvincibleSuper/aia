package my.jwds.core.api;

import my.jwds.core.model.ModelProperty;

/**
 * 执行参数
 */
public class InvokeParam extends InvokeDefinition  {

    /**
     * 类别
     */
    private String contentType;

    /**
     * 执行参数的模型
     */
    private ModelProperty model;

    /**
     * 前缀
     */
    private String prefix;


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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public InvokeParam(String definition, String contentType, ModelProperty model, String prefix) {
        super(definition);
        this.contentType = contentType;
        this.model = model;
        this.prefix = prefix;
    }

    public InvokeParam() {

    }

    public InvokeParam clone(){
        return new InvokeParam(getDefinition(),getContentType(),getModel().clone(),getPrefix());
    }
}
