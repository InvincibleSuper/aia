package com.github.aia.core.template;

import com.github.aia.core.AiaContext;

/**
 * 模板生成器，在api扫描器扫描到所有api后生成模板
 */
public interface TemplateGenerator {

    /**
     * 生成模板
     * @param aiaContext
     */
    void generate(AiaContext aiaContext);



    /**
     * 生成模板
     * @param aiaContext
     */
    AiaTemplate generate(AiaContext aiaContext,String api);

}
