package com.github.aia.cache;


/**
 * 软引用
 */
public class SoftCacheManager extends AbstractCacheManager{


    /**
     * 创建一个缓存实例
     *
     * @return
     */
    @Override
    protected Cache createCacheInstance() {
        return new SoftMapCache();
    }
}
