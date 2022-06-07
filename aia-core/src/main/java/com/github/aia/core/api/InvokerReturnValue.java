package com.github.aia.core.api;

import com.github.aia.core.model.ModelProperty;

/**
 * 调用返回值
 */
public class InvokerReturnValue extends InvokeDefinition {

    /**
     * 返回模型
     */
    private ModelProperty returnModel;


    /**
     * 示例
     */
    private String example;


    /**
     * 数据还是页面
     */
    private boolean dataOfPage;

    public InvokerReturnValue(ModelProperty returnModel, String example, boolean dataOfPage) {
        this.returnModel = returnModel;
        this.example = example;
        this.dataOfPage = dataOfPage;
    }

    public InvokerReturnValue(String definition, ModelProperty returnModel, String example, boolean dataOfPage) {
        super(definition);
        this.returnModel = returnModel;
        this.example = example;
        this.dataOfPage = dataOfPage;
    }

    public InvokerReturnValue() {
    }

    public ModelProperty getReturnModel() {
        return returnModel;
    }

    public void setReturnModel(ModelProperty returnModel) {
        this.returnModel = returnModel;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isDataOfPage() {
        return dataOfPage;
    }

    public void setDataOfPage(boolean dataOfPage) {
        this.dataOfPage = dataOfPage;
    }

    public InvokerReturnValue clone(){
        return new InvokerReturnValue(getDefinition(),getReturnModel().clone(),getExample(),isDataOfPage());
    }
}
