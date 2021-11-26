package my.jwds.core.plugin.mgt;

import my.jwds.core.plugin.AiaPlugin;

import java.util.List;

public interface AiaPluginManager {

    /**
     * 注册插件
     * @param plugin
     */
    void addPlugin(AiaPlugin plugin);

    /**
     * 获取全部插件
     * @return
     */
    List<AiaPlugin> allPlugin();
}
