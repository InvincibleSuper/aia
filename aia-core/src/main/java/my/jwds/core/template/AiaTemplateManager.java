package my.jwds.core.template;

import my.jwds.core.api.InvokeUrl;

import java.util.List;
import java.util.Map;

/**
 * 模板管理器
 */
public interface AiaTemplateManager {

    /**
     * 添加模板
     * @param template 模板
     */
    void addTemplate(AiaTemplate template);

    /**
     * 删除模板
     * @param template 模板
     */
    void removeTemplate(AiaTemplate template);

    /**
     * 修改模板
     * @param name 名称
     * @param template 模板
     */
    void updateTemplate(AiaTemplate template);

    /**
     * 全部模板
     * @return 全部模板
     */
    Map<String,Map<String,AiaTemplate>> allTemplate();

    /**
     * 根据组获取模板
     * @param group 根据组获取模板
     * @return
     */
    Map<String,AiaTemplate> getGroupTemplate(String group);


    /**
     * 获取组对应模板map
     * @return
     */
    Map<String, List<AiaTemplate>> getGroupTemplateMap();
}
