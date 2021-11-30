package my.jwds.core.api.mgt;

import my.jwds.cache.Cache;
import my.jwds.cache.CacheManager;
import my.jwds.core.api.DefaultApiComparator;
import my.jwds.core.api.InvokeApi;

import java.util.*;

public class DefaultAiaApiManager implements AiaApiManager {


    private Cache<String,List<InvokeApi>> cache;

    private Comparator<InvokeApi> comparator;

    private static final String cacheName = "API_CACHE";

    public DefaultAiaApiManager(CacheManager cacheManager){
        cache = cacheManager.getCache(cacheName);
    }


    /**
     * 设置一个Api排序比较器
     *
     * @param comparator
     */
    @Override
    public void setComparator(Comparator<InvokeApi> comparator) {
        this.comparator = comparator;
    }

    public Comparator<InvokeApi> getComparator() {
        if (comparator == null)comparator = new DefaultApiComparator();
        return comparator;
    }

    /**
     * 注册api
     *
     * @param api
     */
    @Override
    public void addApi(InvokeApi api) {
        List<InvokeApi> apis = cache.get(api.getGroup());
        if (apis == null){
            apis = new ArrayList<>();
            cache.put(api.getGroup(),apis);
        }
        apis.add(api);
        apis.sort(getComparator());
    }

    /**
     * 注册api
     *
     * @param group
     * @param apis
     */
    @Override
    public void addGroupApi(String group, List<InvokeApi> apis) {
        List<InvokeApi> oldApis = cache.get(group);
        if (oldApis == null){
            oldApis = apis;
            cache.put(group,oldApis);
        }else{
            oldApis.addAll(apis);
        }
        oldApis.sort(getComparator());
    }

    /**
     * 注册所有api
     *
     * @param all 所有api
     */
    @Override
    public void addAll(List<InvokeApi> all) {
        for (InvokeApi api : all) {
            List<InvokeApi> apis = cache.get(api.getGroup());
            if (apis == null){
                apis = new ArrayList<>();
                cache.put(api.getGroup(),apis);
            }
            apis.sort(getComparator());
            apis.add(api);
        }
    }


    /**
     * 注册api
     *
     * @param all
     */
    @Override
    public void addAll(Map<String, List<InvokeApi>> all) {
        for (Map.Entry<String, List<InvokeApi>> entry : all.entrySet()) {
            entry.getValue().sort(getComparator());
            cache.put(entry.getKey(),entry.getValue());
        }
    }


    /**
     * 获取组
     *
     * @return
     */
    @Override
    public Set<String> getApiGroup() {
        return cache.keys();
    }

    /**
     * 获取组
     *
     * @param group
     * @return
     */
    @Override
    public List<InvokeApi> getGroupInvokeApi(String group) {
        return cache.get(group);
    }

    /**
     * 全部api
     *
     * @return
     */
    @Override
    public Map<String, List<InvokeApi>> allApi() {
        Map<String,List<InvokeApi>> res = new HashMap<>();
        for (String key : cache.keys()) {
            res.put(key,cache.get(key));
        }
        return res;
    }

}
