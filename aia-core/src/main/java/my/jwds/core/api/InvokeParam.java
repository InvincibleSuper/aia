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

    /**
     * 名称
     */
    private String name;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InvokeParam(String definition, String contentType, ModelProperty model, String prefix, String name) {
        super(definition);
        this.contentType = contentType;
        this.model = model;
        this.prefix = prefix;
        this.name = name;
    }

    public InvokeParam() {

    }

    public InvokeParam clone(){
        return new InvokeParam(getDefinition(),getContentType(),getModel().clone(),getPrefix(),getName());
    }
}
