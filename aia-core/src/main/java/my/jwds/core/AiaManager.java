package my.jwds.core;


import my.jwds.api.InvokeApi;
import my.jwds.api.mgt.AiaApiManager;
import my.jwds.definition.resolver.DefinitionResolver;
import my.jwds.model.resolver.ModelResolver;
import my.jwds.plugin.AiaPlugin;
import my.jwds.plugin.mgt.AiaPluginManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AiaManager implements AiaApiScanner,AiaTemplateManager, AiaPluginManager, AiaApiManager {

    private AiaApiScanner apiScanner;

    private AiaTemplateManager templateManager;

    private AiaPluginManager pluginManager;

    private AiaApiManager apiManager;

    private DefinitionResolver definitionResolver;

    private ModelResolver modelResolver;

    public AiaManager(AiaApiScanner apiScanner, AiaTemplateManager templateManager, AiaPluginManager pluginManager, AiaApiManager apiManager, DefinitionResolver definitionResolver, ModelResolver modelResolver) {
        this.apiScanner = apiScanner;
        this.templateManager = templateManager;
        this.pluginManager = pluginManager;
        this.apiManager = apiManager;
        this.definitionResolver = definitionResolver;
        this.modelResolver = modelResolver;
    }

    public AiaManager() {
    }

    public AiaApiScanner getApiScanner() {
        return apiScanner;
    }

    public void setApiScanner(AiaApiScanner apiScanner) {
        this.apiScanner = apiScanner;
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

    public DefinitionResolver getDefinitionResolver() {
        return definitionResolver;
    }

    public void setDefinitionResolver(DefinitionResolver definitionResolver) {
        this.definitionResolver = definitionResolver;
    }

    public ModelResolver getModelResolver() {
        return modelResolver;
    }

    public void setModelResolver(ModelResolver modelResolver) {
        this.modelResolver = modelResolver;
    }


    /**
     * 注册api
     *
     * @param api
     */
    @Override
    public void registerApi(InvokeApi api) {
        apiManager.registerApi(api);
    }

    /**
     * 注册api
     *
     * @param group
     * @param apis
     */
    @Override
    public void registerGroupApi(String group, List<InvokeApi> apis) {
        apiManager.registerGroupApi(group, apis);
    }

    /**
     * 注册api
     *
     * @param all
     */
    @Override
    public void registerAll(Map<String, List<InvokeApi>> all) {
        apiManager.registerAll(all);
    }

    /**
     * 获取组
     *
     * @return
     */
    @Override
    public Set<String> getGroup() {
        return apiManager.getGroup();
    }

    /**
     * 获取组
     *
     * @param group
     * @return
     */
    @Override
    public List<InvokeApi> getGroupInvokeApi(String group) {
        return apiManager.getGroupInvokeApi(group);
    }

    /**
     * 全部api
     *
     * @return
     */
    @Override
    public Map<String, List<InvokeApi>> allApi() {
        return null;
    }

    @Override
    public void startScanner(AiaManager manager) {

    }

    @Override
    public void add(AiaTemplate template) {

    }

    @Override
    public void remove(AiaTemplate template) {

    }

    @Override
    public void update(String name, AiaTemplate template) {

    }

    @Override
    public Map<String, Map<String, AiaTemplate>> allTemplate() {
        return null;
    }

    @Override
    public Map<String, AiaTemplate> getGroupTemplate(String group) {
        return null;
    }

    @Override
    public void registerPlugin(AiaPlugin plugin) {

    }

    @Override
    public List<AiaPlugin> allPlugin() {
        return null;
    }
}
