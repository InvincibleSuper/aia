package my.jwds.springweb;

import my.jwds.config.AiaConfig;
import my.jwds.definition.resolver.DefinitionResolver;
import my.jwds.definition.resolver.JavadocDefinitionResolver;
import my.jwds.definition.resolver.PriorityDefinitionResolver;
import my.jwds.utils.ClassUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiaSpringWebConfigure {

    @Bean
    public AiaConfig aiaConfig(){
        AiaConfig config = new AiaConfig();
        config.setPersist(true);
        config.setSrcPath(ClassUtil.originPath());
        return config;
    }


    @Bean
    public DefinitionResolver definitionResolver(AiaConfig aiaConfig){
        return new PriorityDefinitionResolver(new JavadocDefinitionResolver(aiaConfig.getSrcPath()));
    }



}
