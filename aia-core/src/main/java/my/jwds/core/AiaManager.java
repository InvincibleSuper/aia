package my.jwds.core;


import my.jwds.core.api.InvokeApi;
import my.jwds.core.api.InvokeUrl;
import my.jwds.core.api.mgt.AiaApiManager;
import my.jwds.core.plugin.AiaPlugin;
import my.jwds.core.plugin.mgt.AiaPluginManager;
import my.jwds.core.template.AiaTemplate;
import my.jwds.core.template.AiaTemplateManager;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 总管理类，包含 模板管理器、插件管理器、api管理器
 */
public class AiaManager implements AiaTemplateManager, AiaPluginManager, AiaApiManager {


    private AiaTemplateManager templateManager;

    private AiaPluginManager pluginManager;

    private AiaApiManager apiManager;

    private boolean scan;


    public AiaManager(AiaTemplateManager templateManager, AiaPluginManager pluginManager, AiaApiManager apiManager) {
        this.templateManager = templateManager;
        this.pluginManager = pluginManager;
        this.apiManager = apiManager;
    }

    public AiaManager() {
    }



    public AiaTemplateManager getTemplateManager() {
        return templateManager;
    }

    public void setTemplateManager(AiaTemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public AiaPluginManager getPluginManager() {
        return pluginManager;
    }

    public void setPluginManager(AiaPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public AiaApiManager getApiManager() {
        return apiManager;
    }

    public void setApiManager(AiaApiManager apiManager) {
        this.apiManager = apiManager;
    }


    /**
     * 添加模板
     *
     * @param template 模板
     */
    @Override
    public void addTemplate(AiaTemplate template) {
        templateManager.addTemplate(template);
    }

    /**
     * 删除模板
     *
     * @param template 模板
     */
    @Override
    public void removeTemplate(AiaTemplate template) {

    }

    /**
     * 修改模板
     *
     * @param name     名称
     * @param template 模板
     */
    @Override
    public void updateTemplate(String name, AiaTemplate template) {

    }

    /**
     * 全部模板
     *
     * @return 全部模板
     */
    @Override
    public Map<String, Map<InvokeUrl, AiaTemplate>> allTemplate() {
        return templateManager.allTemplate();
    }

    /**
     * 根据组获取模板
     *
     * @param group 根据组获取模板
     * @return
     */
    @Override
    public Map<InvokeUrl, AiaTemplate> getGroupTemplate(String group) {
        return null;
    }

    /**
     * 注册api
     *
     * @param api api
     */
    @Override
    public void addApi(InvokeApi api) {

    }

    /**
     * 根据组注册api列表
     *
     * @param group 组
     * @param apis  api列表
     */
    @Override
    public void addGroupApi(String group, List<InvokeApi> apis) {

    }

    /**
     * 注册所有api
     *
     * @param all 所有api
     */
    @Override
    public void addAll(List<InvokeApi> all) {
        getApiManager().addAll(all);
    }

    /**
     * 注册所有api
     *
     * @param all 所有api
     */
    @Override
    public void addAll(Map<String, List<InvokeApi>> all) {

    }

    /**
     * 获取组
     *
     * @return 组列表
     */
    @Override
    public Set<String> getApiGroup() {
        return null;
    }

    /**
     * 获取组
     *
     * @param group 组名
     * @return
     */
    @Override
    public List<InvokeApi> getGroupInvokeApi(String group) {
        return null;
    }

    /**
     * 全部api
     *
     * @return 组和api的键值对
     */
    @Override
    public Map<String, List<InvokeApi>> allApi() {
        return getApiManager().allApi();
    }

    /**
     * 注册插件
     *
     * @param plugin
     */
    @Override
    public void addPlugin(AiaPlugin plugin) {

    }

    /**
     * 获取全部插件
     *
     * @return
     */
    @Override
    public List<AiaPlugin> allPlugin() {
        return null;
    }

    /**
     * 设置一个Api排序比较器
     *
     * @param comparator
     */
    @Override
    public void setComparator(Comparator<InvokeApi> comparator) {
        apiManager.setComparator(comparator);
    }

    public boolean isScan() {
        return scan;
    }

    public void setScan(boolean scan) {
        this.scan = scan;
    }


    @Override
    public Map<String, List<AiaTemplate>> getGroupTemplateMap() {
        return templateManager.getGroupTemplateMap();
    }
}
