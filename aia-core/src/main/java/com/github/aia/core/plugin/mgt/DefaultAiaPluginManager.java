package com.github.aia.core.plugin.mgt;

import com.github.aia.cache.Cache;
import com.github.aia.cache.CacheManager;
import com.github.aia.core.plugin.AiaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DefaultAiaPluginManager implements AiaPluginManager {

    private Cache<String,AiaPlugin> cache;

    private static final String PLUGIN_CACHE = "plugin_cache";


    public DefaultAiaPluginManager(CacheManager manager) {
        this.cache = manager.getCache(PLUGIN_CACHE);
    }

    @Override
    public void addPlugin(AiaPlugin plugin) {
        cache.put(plugin.getName(),plugin);
    }

    @Override
    public List<AiaPlugin> allPlugin() {
        return new ArrayList<>( cache.values());
    }
}
