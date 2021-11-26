package my.jwds.springweb;

import my.jwds.core.api.mgt.AiaApiManager;
import my.jwds.core.api.mgt.DefaultAiaApiManager;
import my.jwds.cache.CacheManager;
import my.jwds.cache.DefaultCacheManager;
import my.jwds.config.AiaConfig;
import my.jwds.core.AiaApiScanner;
import my.jwds.core.AiaManager;
import my.jwds.core.AiaTemplateManager;
import my.jwds.core.DefaultAiaTemplateManager;
import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.api.definition.resolver.JavadocDefinitionResolver;
import my.jwds.core.api.definition.resolver.PriorityDefinitionResolver;
import my.jwds.core.model.resolver.DefaultModelResolver;
import my.jwds.core.model.resolver.ModelResolver;
import my.jwds.core.plugin.mgt.AiaPluginManager;
import my.jwds.core.plugin.mgt.DefaultAiaPluginManager;
import my.jwds.springweb.data.AiaController;
import my.jwds.springweb.parse.RequestMappingHandlerMappingParser;
import my.jwds.springweb.parse.SpringHandlerMappingParserComposite;
import my.jwds.springweb.parse.method.*;
import my.jwds.utils.ClassUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;


@Configuration
public class AiaSpringWebConfigure {


    public AiaConfig aiaConfig(){
        return new AiaConfig(true, ClassUtils.originPath());
    }


    public CacheManager cacheManager(){
        return new DefaultCacheManager();
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


    public AiaManager aiaManager(AiaTemplateManager templateManager,AiaPluginManager pluginManager,AiaApiManager apiManager){
        return new AiaManager(templateManager,pluginManager,apiManager);
    }




    public PriorityDefinitionResolver priorityDefinitionResolver(List<DefinitionResolver> definitionResolvers){
        return new PriorityDefinitionResolver(definitionResolvers);
    }


    public ModelResolver defaultModelResolver(DefinitionResolver priorityDefinitionResolver){
        return new DefaultModelResolver(priorityDefinitionResolver);
    }


    public HandlerMethodArgumentResolverRegister handlerMethodArgumentResolverRegister(DefinitionResolver definitionResolver, ModelResolver modelResolver){
        HandlerMethodArgumentResolverRegister resolverRegister = new DefaultHandlerMethodArgumentResolverRegister();
        HandlerMethodArgumentResolver ignore = new IgnoreHandlerMethodArgumentResolver();
        HandlerMethodArgumentResolver file = new MultipartFileHandlerMethodArgumentResolver(definitionResolver,modelResolver);
        HandlerMethodArgumentResolver param = new ParamHandlerMethodArgumentResolver(definitionResolver,modelResolver);
        HandlerMethodArgumentResolver requestBody = new RequestBodyHandlerMethodArgumentResolver(definitionResolver,modelResolver);
        HandlerMethodArgumentResolver requestParam = new RequestParamHandlerMethodArgumentResolver(definitionResolver,modelResolver);
        resolverRegister.registerResolver(ignore);
        resolverRegister.registerResolver(file);
        resolverRegister.registerResolver(param);
        resolverRegister.registerResolver(requestBody);
        resolverRegister.registerResolver(requestParam);
        return resolverRegister;
    }

    public SpringHandlerMappingParserComposite parserComposite(DefinitionResolver definitionResolver, HandlerMethodArgumentResolverRegister argumentResolverRegister){
        SpringHandlerMappingParserComposite parserComposite = new SpringHandlerMappingParserComposite();
        parserComposite.addParser(new RequestMappingHandlerMappingParser(definitionResolver,argumentResolverRegister));
        return parserComposite;
    }


    @Bean
    public AiaApiScanner aiaApiScanner(ApplicationContext context){
        AiaConfig aiaConfig = aiaConfig();
        CacheManager cacheManager = cacheManager();
        AiaManager aiaManager = aiaManager(aiaTemplateManager(cacheManager),aiaPluginManager(cacheManager),aiaApiManager(cacheManager));
        JavadocDefinitionResolver javadocDefinitionResolver = new JavadocDefinitionResolver(aiaConfig.getSrcPath());
        DefinitionResolver definitionResolver = priorityDefinitionResolver(Collections.singletonList(javadocDefinitionResolver));
        ModelResolver modelResolver = defaultModelResolver(definitionResolver);
        HandlerMethodArgumentResolverRegister argumentResolverRegister = handlerMethodArgumentResolverRegister(definitionResolver,modelResolver);
        SpringHandlerMappingParserComposite parserComposite = parserComposite(definitionResolver,argumentResolverRegister);
        return new SpringWebAiaScanner(context,parserComposite,aiaManager);
    }

    @Bean
    public WebMvcConfigurer aiaPageConfigurer(){
        return new AiaPageConfigurer();
    }

    @Bean
    public AiaController aiaController(AiaApiScanner aiaApiScanner){
        AiaController aiaController = new AiaController();
        aiaController.setAiaManager(aiaApiScanner.getAiaManager());
        return aiaController;
    }
}
