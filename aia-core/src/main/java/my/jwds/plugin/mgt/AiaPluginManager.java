package my.jwds.plugin.mgt;

import my.jwds.plugin.AiaPlugin;

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
