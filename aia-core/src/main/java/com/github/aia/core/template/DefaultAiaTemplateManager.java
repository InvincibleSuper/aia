package com.github.aia.core.template;

import com.github.aia.cache.Cache;
import com.github.aia.cache.CacheManager;

import java.util.*;

public class DefaultAiaTemplateManager implements AiaTemplateManager{



    private Cache<String, Map<String, AiaTemplate>> cache;

    private Comparator<AiaTemplate> templateComparator = new DefaultTemplateComparator();

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
    public void updateTemplate(AiaTemplate template) {
        ensureContent(template.getGroup()).put(template.getName(), template);
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

    @Override
    public Map<String, List<AiaTemplate>> getGroupTemplateMap() {
        Map<String, List<AiaTemplate>> res = new LinkedHashMap<>();
        Map<String, Map<String, AiaTemplate>> allTemplate = allTemplate();
        for (Map.Entry<String, Map<String, AiaTemplate>> entry : allTemplate.entrySet()) {
            List<AiaTemplate> list = new ArrayList(entry.getValue().values());
            list.sort(templateComparator);
            res.put(entry.getKey(),list);
        }
        return res;
    }


    /**
     * 是否包含此模板
     *
     * @param template
     * @return
     */
    @Override
    public boolean contains(AiaTemplate template) {
        Map map = getGroupTemplate(template.getGroup());


        return map != null && map.containsKey(template.getName());
    }
}
