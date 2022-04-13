package my.jwds.core.template;

import my.jwds.core.AiaManager;

/**
 * 模板生成器，在api扫描器扫描到所有api后生成模板
 */
public interface TemplateGenerator {

    /**
     * 生成模板
     * @param aiaManager
     */
    void generate(AiaManager aiaManager);



    /**
     * 生成模板
     * @param aiaManager
     */
    AiaTemplate generate(AiaManager aiaManager,String api);

}
