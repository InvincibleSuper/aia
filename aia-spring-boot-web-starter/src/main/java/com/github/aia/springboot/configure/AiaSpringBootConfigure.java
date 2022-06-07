package com.github.aia.springboot.configure;

import com.github.aia.core.AiaApiScanner;
import com.github.aia.core.AiaContext;
import com.github.aia.core.template.AiaTemplateManager;
import com.github.aia.core.template.TemplateGenerator;
import com.github.aia.springweb.AiaSpringWebConfigure;
import com.github.aia.core.api.definition.resolver.DefinitionResolver;
import com.github.aia.core.api.definition.resolver.JavadocDefinitionResolver;
import com.github.aia.core.api.definition.resolver.PriorityDefinitionResolver;
import com.github.aia.core.api.mgt.AiaApiManager;
import com.github.aia.cache.CacheManager;
import com.github.aia.config.AiaConfig;
import com.github.aia.core.security.SecuritySupport;
import com.github.aia.core.model.resolver.ModelResolver;
import com.github.aia.core.plugin.mgt.AiaPluginManager;
import com.github.aia.springweb.SpringWebAiaScanner;
import com.github.aia.springweb.parse.RequestMappingHandlerMappingParser;
import com.github.aia.springweb.parse.SpringHandlerMappingParserComposite;
import com.github.aia.springweb.parse.method.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "aia",name = "enable",havingValue = "true",matchIfMissing = true)
public class AiaSpringBootConfigure extends AiaSpringWebConfigure {


    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("aia")
    public AiaConfig aiaConfig(){
        return super.aiaConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(){
        return super.cacheManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public AiaTemplateManager aiaTemplateManager(CacheManager cacheManager){
        return super.aiaTemplateManager(cacheManager);
    }
    @Bean
    @ConditionalOnMissingBean
    public AiaPluginManager aiaPluginManager(CacheManager cacheManager){
        return super.aiaPluginManager(cacheManager);
    }
    @Bean
    @ConditionalOnMissingBean
    public AiaApiManager aiaApiManager(CacheManager cacheManager){
        return super.aiaApiManager(cacheManager);
    }


    @Bean
    @ConditionalOnMissingBean
    public AiaContext aiaContext(AiaTemplateManager templateManager, AiaPluginManager pluginManager, AiaApiManager apiManager){
        return super.aiaContext(templateManager,pluginManager,apiManager);
    }

    @Bean
    public DefinitionResolver javadocDefinitionResolver(AiaConfig aiaConfig){
        return new JavadocDefinitionResolver(aiaConfig.getSrcPath());
    }

    @Bean
    @ConditionalOnMissingBean
    public PriorityDefinitionResolver priorityDefinitionResolver(List<DefinitionResolver> definitionResolvers){
        return super.priorityDefinitionResolver(definitionResolvers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ModelResolver defaultModelResolver(PriorityDefinitionResolver priorityDefinitionResolver){
        return super.defaultModelResolver(priorityDefinitionResolver);
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
    @ConditionalOnMissingBean
    public SecuritySupport aiaSecuritySupport(AiaConfig config){
        return super.aiaSecuritySupport(config);
    }


    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodArgumentResolverRegister handlerMethodArgumentResolverRegister(List<HandlerMethodArgumentResolver> argumentResolvers){
        HandlerMethodArgumentResolverRegister argumentResolverRegister = new DefaultHandlerMethodArgumentResolverRegister();
        argumentResolverRegister.registerResolvers(argumentResolvers);
        return argumentResolverRegister;
    }



    @Bean
    @ConditionalOnMissingBean
    public SpringHandlerMappingParserComposite parserComposite(PriorityDefinitionResolver definitionResolver
            ,ModelResolver modelResolver
            , HandlerMethodArgumentResolverRegister argumentResolverRegister
            ,SecuritySupport securitySupport){
        SpringHandlerMappingParserComposite parserComposite = new SpringHandlerMappingParserComposite();
        parserComposite.addParser(new RequestMappingHandlerMappingParser(definitionResolver,modelResolver,argumentResolverRegister,securitySupport));
        return parserComposite;
    }


    @Bean
    @ConditionalOnMissingBean
    public AiaApiScanner aiaApiScanner(ApplicationContext applicationContext, SpringHandlerMappingParserComposite parserComposite, AiaContext aiaContext){
        return new SpringWebAiaScanner(applicationContext,parserComposite,aiaContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public TemplateGenerator templateGenerator(){
        return super.templateGenerator();
    }
}
