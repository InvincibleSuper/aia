package my.jwds.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 软引用Map缓存，内存不足抛出异常
 * 此类是为了内存不足对用户做出提醒
 * @param <K>
 * @param <V>
 */
public class SoftMapCache<K,V> implements Cache<K,V>{

    private SoftReference<Map> softReference;
    private ReferenceQueue<Map> referenceQueue;
    public SoftMapCache() {
        referenceQueue = new ReferenceQueue<>();
        softReference = new SoftReference<>(new ConcurrentHashMap<>(),referenceQueue);
    }



    protected Map<K,V> getMapNoGc(){
        Map<K,V> map = softReference.get();
        if (map == null)throwException();
        return map;
    }

    protected void throwException(){
        throw new CacheException("内存不足，缓存已被GC，请分配足够的空间重启后试一试。");
    }


    @Override
    public V get(K key) throws CacheException {
        Map<K,V> map = getMapNoGc();
        return map.get(key);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        Map<K,V> map = getMapNoGc();
        return map.put(key, value);
    }

    @Override
    public V remove(K key) throws CacheException {
        Map<K,V> map = getMapNoGc();
        return map.remove(key);
    }

    @Override
    public void clear() throws CacheException {
        Map<K,V> map = getMapNoGc();
        map.clear();
    }

    @Override
    public int size() throws CacheException {
        Map<K,V> map = getMapNoGc();
        return map.size();
    }

    @Override
    public Set<K> keys() throws CacheException {
        Map<K,V> map = getMapNoGc();
        return map.keySet();
    }

    @Override
    public Collection<V> values() throws CacheException {
        Map<K,V> map = getMapNoGc();
        return map.values();
    }


}
