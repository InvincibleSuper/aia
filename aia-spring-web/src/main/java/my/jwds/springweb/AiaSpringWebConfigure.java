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
import my.jwds.definition.resolver.DefinitionResolver;
import my.jwds.definition.resolver.JavadocDefinitionResolver;
import my.jwds.definition.resolver.PriorityDefinitionResolver;
import my.jwds.model.resolver.DefaultModelResolver;
import my.jwds.model.resolver.ModelResolver;
import my.jwds.plugin.mgt.AiaPluginManager;
import my.jwds.plugin.mgt.DefaultAiaPluginManager;
import my.jwds.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AiaSpringWebConfigure {

    @Autowired
    private AiaManager aiaManager;

    @Autowired
    private AiaApiScanner aiaApiScanner;

    @Bean
    public AiaConfig aiaConfig(){
        AiaConfig config = new AiaConfig();
        config.setPersist(true);
        config.setSrcPath(ClassUtil.originPath());
        return config;
    }

    @Bean
    public CacheManager cacheManager(){
        return new SoftCacheManager();
    }

    @Bean
    public AiaManager aiaManager(CacheManager cacheManager){
        AiaTemplateManager templateManager = new DefaultAiaTemplateManager(cacheManager);
        AiaPluginManager pluginManager = new DefaultAiaPluginManager(cacheManager);
        AiaApiManager apiManager = new DefaultAiaApiManager(cacheManager);
        AiaManager aiaManager = new AiaManager(templateManager,pluginManager,apiManager);
        return aiaManager;
    }


    @Bean
    public AiaApiScanner aiaApiScanner(AiaConfig aiaConfig, ApplicationContext applicationContext){
        DefinitionResolver definitionResolver = new PriorityDefinitionResolver(new JavadocDefinitionResolver(aiaConfig.getSrcPath()));;
        ModelResolver modelResolver = new DefaultModelResolver(definitionResolver);
        AiaApiScanner apiScanner = new SpringWebAiaScanner(applicationContext,definitionResolver,modelResolver);
        return apiScanner;
    }

    @PostConstruct
    public void start(){
        aiaApiScanner.startScanner(aiaManager);
    }

}
