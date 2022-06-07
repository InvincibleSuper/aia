package com.github.aia.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认缓存 使用 ConcurrentHashMap 存储
 * @param <K>
 * @param <V>
 */
public class DefaultCache<K,V> implements Cache<K,V>{


    private ConcurrentHashMap<K,V> cache = new ConcurrentHashMap<>();


    @Override
    public V get(K key) throws CacheException {
        return cache.get(key);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        return cache.put(key,value);
    }

    @Override
    public V remove(K key) throws CacheException {
        return cache.remove(key);
    }

    @Override
    public void clear() throws CacheException {
        cache.clear();
    }

    @Override
    public int size() throws CacheException {
        return cache.size();
    }

    @Override
    public Set<K> keys() throws CacheException {
        return cache.keySet();
    }

    @Override
    public Collection<V> values() throws CacheException {
        return cache.values();
    }
}
