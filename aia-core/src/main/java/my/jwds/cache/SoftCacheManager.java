package my.jwds.cache;

import java.util.concurrent.ConcurrentHashMap;

public class SoftCacheManager implements CacheManager{

    private ConcurrentHashMap<String,Cache> caches = new ConcurrentHashMap();

    @Override
    public  <K, V> Cache<K, V> getCache(String name) {
        Cache cache = caches.get(name);
        if (cache == null){
            synchronized(this){
                if ((cache = caches.get(name)) == null){
                    cache = new MapCache();
                    caches.put(name,cache);
                }
            }
        }
        return cache;
    }
}
