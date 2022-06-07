package com.github.aia.core;

import com.github.aia.core.template.TemplateGenerator;
import com.github.aia.core.template.DefaultTemplateGenerator;
import com.github.aia.exception.AiaException;

/**
 * 抽象的api扫描器，将扫描委托给子类，扫描后使用模板生成器生成模板
 */
public abstract class AbstractApiScanner implements AiaApiScanner {



    private AiaContext aiaContext;


    private TemplateGenerator templateGenerator;


    /**
     * 扫描所有api
     */
    @Override
    public void startScanner() {
        scanning();
        new Thread(()->{
            while(!getAiaContext().isScan()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            getTemplateGenerator().generate(aiaContext);
            getAiaContext().setGenerateTemplate(true);
        }).start();
    }


    protected abstract void scanning();

    /**
     * 获取aia管理器
     *
     * @return aia管理器
     */
    @Override
    public AiaContext getAiaContext() {
        if (aiaContext == null){
            throw new AiaException("aiaManager不能为空");
        }
        return aiaContext;
    }

    public void setAiaContext(AiaContext aiaContext) {
        this.aiaContext = aiaContext;
    }

    public TemplateGenerator getTemplateGenerator() {
        if (templateGenerator == null){
            templateGenerator = new DefaultTemplateGenerator();
        }
        return templateGenerator;
    }

    public void setTemplateGenerator(TemplateGenerator templateGenerator) {
        this.templateGenerator = templateGenerator;
    }
}
