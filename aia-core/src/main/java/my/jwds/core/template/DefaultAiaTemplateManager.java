package my.jwds.core.template;

import my.jwds.cache.Cache;
import my.jwds.cache.CacheManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultAiaTemplateManager implements AiaTemplateManager{



    private Cache<String, Map<String, AiaTemplate>> cache;



    private static final String TEMPLATE_CACHE = "template_cache";


    public DefaultAiaTemplateManager(CacheManager manager) {
        this.cache = manager.getCache(TEMPLATE_CACHE);
    }

    @Override
    public void addTemplate(AiaTemplate template) {
        ensureContent(template.getGroup()).put(template.getName(),template);
    }

    @Override
    public void removeTemplate(AiaTemplate template) {
        ensureContent(template.getGroup()).remove(template.getName(),template);
    }

    @Override
    public void updateTemplate(String name, AiaTemplate template) {
        ensureContent(template.getGroup()).put(template.getName(),template);
    }

    @Override
    public Map<String, Map<String, AiaTemplate>> allTemplate() {
        Map<String, Map<String, AiaTemplate>> res = new LinkedHashMap<>();
        for (String key : cache.keys()) {
            res.put(key,cache.get(key));
        }
        return res;
    }

    @Override
    public Map<String, AiaTemplate> getGroupTemplate(String group) {
        return cache.get(group);
    }

    private Map<String,AiaTemplate> ensureContent(String group){
        Map<String,AiaTemplate> res = cache.get(group);
        if (res == null){
            res = new LinkedHashMap<>();
            cache.put(group,res);
        }
        return res;
    }
}
