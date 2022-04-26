package my.jwds.springweb;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 页面映射
 */
public class AiaPageConfigurer implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //registry.addResourceHandler("/aia/**").addResourceLocations("classpath:/aia/");
        registry.addResourceHandler("/aia/**").addResourceLocations("file:E:\\java\\workSpace\\aia\\aia-page-ui\\src\\main\\resources\\aia\\");
    }
}
