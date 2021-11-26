package my.jwds.springboot.samples.config;

import my.jwds.cache.CacheManager;
import my.jwds.cache.SoftCacheManager;
import my.jwds.springweb.AiaSpringWebConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

@Configuration
public class MyConfig {



    @Bean
    @ConditionalOnMissingBean
    public CacheManager object(){
        return new SoftCacheManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager jj(){
        return new SoftCacheManager();
    }

}
