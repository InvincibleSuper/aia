package my.jwds.core;

import my.jwds.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultAiaTemplateManager implements AiaTemplateManager{


    private Cache<String,Map<String,AiaTemplate>> cache;


    public DefaultAiaTemplateManager(Cache<String, Map<String, AiaTemplate>> cache) {
        this.cache = cache;
    }

    @Override
    public void add(AiaTemplate template) {
        ensureContent(template.getGroup()).put(template.getName(),template);
    }

    @Override
    public void remove(AiaTemplate template) {
        ensureContent(template.getGroup()).remove(template.getName(),template);
    }

    @Override
    public void update(String name, AiaTemplate template) {
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
