package com.github.aia.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public interface Cache<K,V> extends Serializable {


    V get(K key) throws CacheException;


    V put(K key, V value) throws CacheException;


    V remove(K key) throws CacheException;


    void clear() throws CacheException;


    public int size() throws CacheException;


    Set<K> keys() throws CacheException;


    Collection<V> values() throws CacheException;


}
