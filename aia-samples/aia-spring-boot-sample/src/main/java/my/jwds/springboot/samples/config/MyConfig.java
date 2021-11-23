package my.jwds.springboot.samples.config;

import my.jwds.cache.CacheManager;
import my.jwds.cache.SoftCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
