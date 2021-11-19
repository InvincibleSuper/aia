package my.jwds.springweb;

import my.jwds.api.mgt.AiaApiManager;
import my.jwds.api.mgt.DefaultAiaApiManager;
import my.jwds.cache.CacheManager;
import my.jwds.cache.SoftCacheManager;
import my.jwds.config.AiaConfig;
import my.jwds.core.AiaApiScanner;
import my.jwds.core.AiaManager;
import my.jwds.core.AiaTemplateManager;
import my.jwds.core.DefaultAiaTemplateManager;
import my.jwds.api.definition.resolver.DefinitionResolver;
import my.jwds.api.definition.resolver.JavadocDefinitionResolver;
import my.jwds.api.definition.resolver.PriorityDefinitionResolver;
import my.jwds.model.ModelProperty;
import my.jwds.model.resolver.DefaultModelResolver;
import my.jwds.model.resolver.ModelResolver;
import my.jwds.plugin.mgt.AiaPluginManager;
import my.jwds.plugin.mgt.DefaultAiaPluginManager;
import my.jwds.springweb.parse.SpringHandlerMappingParser;
import my.jwds.springweb.parse.method.HandlerMethodPropertiesResolver;
import my.jwds.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Configuration
public class AiaSpringWebConfigure {



    public AiaConfig aiaConfig(){
        AiaConfig config = new AiaConfig(true,ClassUtil.originPath());
        return config;
    }


    public CacheManager cacheManager(){
        return new SoftCacheManager();
    }


    public AiaTemplateManager aiaTemplateManager(CacheManager cacheManager){
        return new DefaultAiaTemplateManager(cacheManager);
    }

    public AiaPluginManager aiaPluginManager(CacheManager cacheManager){
        return new DefaultAiaPluginManager(cacheManager);
    }

    public AiaApiManager aiaApiManager(CacheManager cacheManager){
        return new DefaultAiaApiManager(cacheManager);
    }

    public AiaManager aiaManager(CacheManager cacheManager){
        AiaTemplateManager templateManager = aiaTemplateManager(cacheManager);
        AiaPluginManager pluginManager = aiaPluginManager(cacheManager);
        AiaApiManager apiManager =  aiaApiManager(cacheManager);
        AiaManager aiaManager = new AiaManager(templateManager,pluginManager,apiManager);
        return aiaManager;
    }


    public DefinitionResolver javadocDefinitionResolver(AiaConfig aiaConfig){
        return new JavadocDefinitionResolver(aiaConfig.getSrcPath());
    }


    public DefinitionResolver priorityDefinitionResolver(DefinitionResolver... definitionResolvers){
        return new PriorityDefinitionResolver(definitionResolvers);
    }


    public ModelResolver resolver(DefinitionResolver priorityDefinitionResolver){
        return new DefaultModelResolver(priorityDefinitionResolver);
    }


    public HandlerMethodPropertiesResolver handlerMethodPropertiesResolver(DefinitionResolver definitionResolver, ModelProperty modelProperty){
        return null;
    }

    public SpringHandlerMappingParser requestMappingHandlerMappingParser(DefinitionResolver definitionResolver, HandlerMethodPropertiesResolver handlerMethodPropertiesResolver){
        return null;
    }


    public AiaApiScanner aiaApiScanner(){
        SpringWebAiaScanner apiScanner = new SpringWebAiaScanner();
        return apiScanner;
    }



}
