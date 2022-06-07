package com.github.aia.springboot.samples.config;

import com.github.aia.cache.SoftCacheManager;
import com.github.aia.cache.CacheManager;
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
