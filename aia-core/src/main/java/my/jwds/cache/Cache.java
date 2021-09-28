package my.jwds.cache;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Map;
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
