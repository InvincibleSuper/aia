package my.jwds.core;

import my.jwds.core.api.mgt.AiaApiManager;
import my.jwds.core.plugin.mgt.AiaPluginManager;
import my.jwds.core.template.AiaTemplateManager;

public class AiaContext {
    public AiaContext(AiaTemplateManager templateManager, AiaPluginManager pluginManager, AiaApiManager apiManager) {
        this.templateManager = templateManager;
        this.pluginManager = pluginManager;
        this.apiManager = apiManager;
    }

    public AiaContext() {
    }

    private AiaTemplateManager templateManager;

    private AiaPluginManager pluginManager;

    private AiaApiManager apiManager;

    private boolean scan;

    private boolean generateTemplate;

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

    public boolean isScan() {
        return scan;
    }

    public void setScan(boolean scan) {
        this.scan = scan;
    }

    public boolean isGenerateTemplate() {
        return generateTemplate;
    }

    public void setGenerateTemplate(boolean generateTemplate) {
        this.generateTemplate = generateTemplate;
    }
}
