package com.github.aia.cache;

import java.io.Serializable;

/**
 * 缓存管理器
 */
public interface CacheManager extends Serializable {

    /**
     * 根据名称获取一个缓存，如果不存在新建一个缓存
     * @param name 名称
     * @param <K> 泛型，键类型
     * @param <V> 泛型，值类型
     * @return
     */
    <K,V> Cache<K,V> getCache(String name);


}
