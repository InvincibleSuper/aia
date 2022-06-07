package com.github.aia.cache;

/**
 * 默认的缓存管理器，返回默认缓存
 */
public class DefaultCacheManager extends AbstractCacheManager{

    /**
     * 创建一个缓存实例
     *
     * @return
     */
    @Override
    protected Cache createCacheInstance() {
        return new DefaultCache();
    }
}
