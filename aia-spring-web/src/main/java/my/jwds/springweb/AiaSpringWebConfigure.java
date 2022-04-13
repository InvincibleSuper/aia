package my.jwds.springweb;

import my.jwds.config.AiaConfigHolder;
import my.jwds.core.api.mgt.AiaApiManager;
import my.jwds.core.api.mgt.DefaultAiaApiManager;
import my.jwds.cache.CacheManager;
import my.jwds.cache.DefaultCacheManager;
import my.jwds.config.AiaConfig;
import my.jwds.core.AiaApiScanner;
import my.jwds.core.AiaManager;
import my.jwds.core.security.QualifiedNameWhiteList;
import my.jwds.core.security.SecuritySupport;
import my.jwds.core.security.UrlWhiteList;
import my.jwds.core.template.AiaTemplateManager;
import my.jwds.core.template.DefaultAiaTemplateManager;
import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.api.definition.resolver.JavadocDefinitionResolver;
import my.jwds.core.api.definition.resolver.PriorityDefinitionResolver;
import my.jwds.core.model.resolver.DefaultModelResolver;
import my.jwds.core.model.resolver.ModelResolver;
import my.jwds.core.plugin.mgt.AiaPluginManager;
import my.jwds.core.plugin.mgt.DefaultAiaPluginManager;
import my.jwds.core.template.DefaultTemplateGenerator;
import my.jwds.core.template.TemplateGenerator;
import my.jwds.springweb.data.AiaController;
import my.jwds.springweb.parse.RequestMappingHandlerMappingParser;
import my.jwds.springweb.parse.SpringHandlerMappingParserComposite;
import my.jwds.springweb.parse.method.*;
import my.jwds.utils.ClassUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;


@Configuration
public class AiaSpringWebConfigure {

    @Bean
    public AiaConfig aiaConfig(){
        AiaConfigHolder.config = new AiaConfig(true, ClassUtils.originPath());
        return AiaConfigHolder.config;
    }

    @Bean
    public SecuritySupport aiaSecuritySupport(AiaConfig config){
        SecuritySupport securitySupport = new SecuritySupport();
        if (config.getPatternQualifiedNames()!= null){
            QualifiedNameWhiteList qualifiedNameWhiteList = new QualifiedNameWhiteList();
            for (String qualifiedName : config.getPatternQualifiedNames()) {
                qualifiedNameWhiteList.addWhiteList(qualifiedName);
            }
            securitySupport.setQualifiedNameWhiteList(qualifiedNameWhiteList);
        }
        if (config.getPatternUrl() != null){
            UrlWhiteList urlWhiteList = new UrlWhiteList();
            for (String url : config.getPatternUrl()) {
                urlWhiteList.addWhiteList(url);
            }
            securitySupport.setUrlWhiteList(urlWhiteList);
        }
        return securitySupport;
    }

    @Bean
    public CacheManager cacheManager(){
        return new DefaultCacheManager();
    }

    @Bean
    public AiaTemplateManager aiaTemplateManager(CacheManager cacheManager){
        return new DefaultAiaTemplateManager(cacheManager);
    }
    @Bean
    public AiaPluginManager aiaPluginManager(CacheManager cacheManager){
        return new DefaultAiaPluginManager(cacheManager);
    }
    @Bean
    public AiaApiManager aiaApiManager(CacheManager cacheManager){
        return new DefaultAiaApiManager(cacheManager);
    }

    @Bean
    public AiaManager aiaManager(AiaTemplateManager templateManager,AiaPluginManager pluginManager,AiaApiManager apiManager){
        return new AiaManager(templateManager,pluginManager,apiManager);
    }
    @Bean
    public DefinitionResolver javadocDefinitionResolver(AiaConfig aiaConfig){
        return new JavadocDefinitionResolver(aiaConfig.getSrcPath());
    }
    @Bean
    public PriorityDefinitionResolver priorityDefinitionResolver(List<DefinitionResolver> definitionResolvers){
        return new PriorityDefinitionResolver(definitionResolvers);
    }

    @Bean
    public ModelResolver defaultModelResolver(PriorityDefinitionResolver priorityDefinitionResolver){
        return new DefaultModelResolver(priorityDefinitionResolver);
    }

    @Bean
    public HandlerMethodArgumentResolver ignoreHandlerMethodArgumentResolver(){
        return new IgnoreHandlerMethodArgumentResolver();
    }

    @Bean
    public HandlerMethodArgumentResolver multipartFileHandlerMethodArgumentResolver(PriorityDefinitionResolver definitionResolver, ModelResolver modelResolver){
        return new MultipartFileHandlerMethodArgumentResolver(definitionResolver,modelResolver);
    }


    @Bean
    public HandlerMethodArgumentResolver paramHandlerMethodArgumentResolver(PriorityDefinitionResolver definitionResolver, ModelResolver modelResolver){
        return new ParamHandlerMethodArgumentResolver(definitionResolver,modelResolver);
    }


    @Bean
    public HandlerMethodArgumentResolver requestBodyHandlerMethodArgumentResolver(PriorityDefinitionResolver definitionResolver, ModelResolver modelResolver){
        return new RequestBodyHandlerMethodArgumentResolver(definitionResolver,modelResolver);
    }

    @Bean
    public HandlerMethodArgumentResolver requestParamHandlerMethodArgumentResolver(PriorityDefinitionResolver definitionResolver, ModelResolver modelResolver){
        return new RequestParamHandlerMethodArgumentResolver(definitionResolver,modelResolver);
    }
    @Bean
    public HandlerMethodArgumentResolver pathVariableHandlerMethodArgumentResolver(PriorityDefinitionResolver definitionResolver, ModelResolver modelResolver){
        return new PathVariableHandlerMethodArgumentResolver(definitionResolver,modelResolver);
    }

    @Bean
    public HandlerMethodArgumentResolverRegister handlerMethodArgumentResolverRegister(List<HandlerMethodArgumentResolver> argumentResolvers){
        HandlerMethodArgumentResolverRegister argumentResolverRegister = new DefaultHandlerMethodArgumentResolverRegister();
        argumentResolverRegister.registerResolvers(argumentResolvers);
        return argumentResolverRegister;
    }
    @Bean
    public SpringHandlerMappingParserComposite parserComposite(PriorityDefinitionResolver definitionResolver
            ,ModelResolver modelResolver
            , HandlerMethodArgumentResolverRegister argumentResolverRegister
            ,SecuritySupport securitySupport){
        SpringHandlerMappingParserComposite parserComposite = new SpringHandlerMappingParserComposite();
        parserComposite.addParser(new RequestMappingHandlerMappingParser(definitionResolver,modelResolver,argumentResolverRegister,securitySupport));
        return parserComposite;
    }


    @Bean
    public AiaApiScanner aiaApiScanner(ApplicationContext applicationContext,SpringHandlerMappingParserComposite parserComposite,AiaManager aiaManager){
        return new SpringWebAiaScanner(applicationContext,parserComposite,aiaManager);
    }

    @Bean
    public WebMvcConfigurer aiaPageConfigurer(){
        return new AiaPageConfigurer();
    }

    @Bean
    public AiaController aiaController(){
        return new AiaController();
    }

    @Bean
    public TemplateGenerator templateGenerator(){
        return new DefaultTemplateGenerator();
    }
}
