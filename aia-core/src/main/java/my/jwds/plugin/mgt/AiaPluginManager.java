package my.jwds.plugin.mgt;

import my.jwds.plugin.AiaPlugin;

import java.util.List;

public interface AiaPluginManager {


    void registerPlugin(AiaPlugin plugin);


    List<AiaPlugin> allPlugin();
}
