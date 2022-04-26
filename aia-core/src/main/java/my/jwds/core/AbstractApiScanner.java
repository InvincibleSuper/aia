package my.jwds.core;

import my.jwds.core.template.DefaultTemplateGenerator;
import my.jwds.core.template.TemplateGenerator;
import my.jwds.exception.AiaException;

/**
 * 抽象的api扫描器，将扫描委托给子类，扫描后使用模板生成器生成模板
 */
public abstract class AbstractApiScanner implements AiaApiScanner {



    private AiaManager aiaManager;


    private TemplateGenerator templateGenerator;


    /**
     * 扫描所有api
     */
    @Override
    public void startScanner() {
        scanning();
        new Thread(()->{
            while(!getAiaManager().isScan()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            getTemplateGenerator().generate(aiaManager);
            getAiaManager().setGenerateTemplate(true);
        }).start();
    }


    protected abstract void scanning();

    /**
     * 获取aia管理器
     *
     * @return aia管理器
     */
    @Override
    public AiaManager getAiaManager() {
        if (aiaManager == null){
            throw new AiaException("aiaManager不能为空");
        }
        return aiaManager;
    }

    /**
     * 设置一个aia管理器
     *
     * @param aiaManager
     */
    @Override
    public void setAiaManager(AiaManager aiaManager) {
        this.aiaManager = aiaManager;
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
