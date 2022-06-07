package com.github.aia.springweb;

import com.github.aia.config.AiaConfigHolder;
import com.github.aia.core.AiaContext;
import com.github.aia.core.api.mgt.AiaApiManager;
import com.github.aia.core.api.mgt.DefaultAiaApiManager;
import com.github.aia.cache.CacheManager;
import com.github.aia.cache.DefaultCacheManager;
import com.github.aia.config.AiaConfig;
import com.github.aia.core.AiaApiScanner;
import com.github.aia.core.security.QualifiedNameWhiteList;
import com.github.aia.core.security.SecuritySupport;
import com.github.aia.core.security.UrlWhiteList;
import com.github.aia.core.template.AiaTemplateManager;
import com.github.aia.core.template.DefaultAiaTemplateManager;
import com.github.aia.core.api.definition.resolver.DefinitionResolver;
import com.github.aia.core.api.definition.resolver.JavadocDefinitionResolver;
import com.github.aia.core.api.definition.resolver.PriorityDefinitionResolver;
import com.github.aia.core.model.resolver.DefaultModelResolver;
import com.github.aia.core.model.resolver.ModelResolver;
import com.github.aia.core.plugin.mgt.AiaPluginManager;
import com.github.aia.core.plugin.mgt.DefaultAiaPluginManager;
import com.github.aia.core.template.DefaultTemplateGenerator;
import com.github.aia.core.template.TemplateGenerator;
import com.github.aia.springweb.data.AiaController;
import com.github.aia.springweb.parse.RequestMappingHandlerMappingParser;
import com.github.aia.springweb.parse.SpringHandlerMappingParserComposite;
import com.github.aia.springweb.parse.method.*;
import com.github.aia.utils.ClassUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
    public AiaContext aiaContext(AiaTemplateManager templateManager, AiaPluginManager pluginManager, AiaApiManager apiManager){
        return new AiaContext(templateManager,pluginManager,apiManager);
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
    public AiaApiScanner aiaApiScanner(ApplicationContext applicationContext,SpringHandlerMappingParserComposite parserComposite,AiaContext aiaContext){
        return new SpringWebAiaScanner(applicationContext,parserComposite,aiaContext);
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
