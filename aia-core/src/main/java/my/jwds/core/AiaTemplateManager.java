package my.jwds.core;

import java.util.Map;

public interface AiaTemplateManager {

    void add(AiaTemplate template);


    void remove(AiaTemplate template);


    void update(String name,AiaTemplate template);


    Map<String,Map<String,AiaTemplate>> allTemplate();


    Map<String,AiaTemplate> getGroupTemplate(String group);



}
