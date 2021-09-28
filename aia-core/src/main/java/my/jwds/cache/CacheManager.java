package my.jwds.cache;

import java.io.Serializable;

public interface CacheManager extends Serializable {


    <K,V> Cache<K,V> getCache(String name);


}
