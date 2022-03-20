package my.jwds.core.template;

import my.jwds.cache.Cache;
import my.jwds.cache.CacheManager;
import my.jwds.core.api.InvokeUrl;

import java.util.*;

public class DefaultAiaTemplateManager implements AiaTemplateManager{



    private Cache<String, Map<InvokeUrl, AiaTemplate>> cache;

    private Comparator<AiaTemplate> templateComparator = new DefaultTemplateComparator();

    private static final String TEMPLATE_CACHE = "template_cache";


    public DefaultAiaTemplateManager(CacheManager manager) {
        this.cache = manager.getCache(TEMPLATE_CACHE);
    }

    @Override
    public void addTemplate(AiaTemplate template) {
        ensureContent(template.getGroup()).put(template.getUrl(),template);

    }

    @Override
    public void removeTemplate(AiaTemplate template) {
        ensureContent(template.getGroup()).remove(template.getName(),template);
    }

    @Override
    public void updateTemplate(String name, AiaTemplate template) {
        ensureContent(template.getGroup()).put(template.getUrl(), template);
    }

    @Override
    public Map<String, Map<InvokeUrl, AiaTemplate>> allTemplate() {
        Map<String, Map<InvokeUrl, AiaTemplate>> res = new LinkedHashMap<>();
        for (String key : cache.keys()) {
            res.put(key,cache.get(key));
        }
        return res;
    }

    @Override
    public Map<InvokeUrl, AiaTemplate> getGroupTemplate(String group) {
        return cache.get(group);
    }

    private Map<InvokeUrl,AiaTemplate> ensureContent(String group){
        Map<InvokeUrl,AiaTemplate> res = cache.get(group);
        if (res == null){
            res = new LinkedHashMap<>();
            cache.put(group,res);
        }
        return res;
    }

    @Override
    public Map<String, List<AiaTemplate>> getGroupTemplateMap() {
        Map<String, List<AiaTemplate>> res = new LinkedHashMap<>();
        Map<String, Map<InvokeUrl, AiaTemplate>> allTemplate = allTemplate();
        for (Map.Entry<String, Map<InvokeUrl, AiaTemplate>> entry : allTemplate.entrySet()) {
            List<AiaTemplate> list = new ArrayList(entry.getValue().values());
            list.sort(templateComparator);
            res.put(entry.getKey(),list);
        }
        return res;
    }
}
