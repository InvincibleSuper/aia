package com.github.aia.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象的缓存管理器，对缓存是否存在进行判断，缓存实例的建立委托给子类
 */
public abstract class AbstractCacheManager implements CacheManager{

    private ConcurrentHashMap<String,Cache> caches = new ConcurrentHashMap();
    /**
     * 根据名称获取一个缓存，如果不存在新建一个缓存
     *
     * @param name 名称
     * @return
     */
    @Override
    public <K, V> Cache<K, V> getCache(String name) {
        Cache cache = caches.get(name);
        if (cache == null){
            synchronized(this){
                if ((cache = caches.get(name)) == null){
                    cache = createCacheInstance();
                    caches.put(name,cache);
                }
            }
        }
        return cache;
    }

    /**
     * 创建一个缓存实例
     * @return
     */
    protected abstract Cache createCacheInstance();

}
