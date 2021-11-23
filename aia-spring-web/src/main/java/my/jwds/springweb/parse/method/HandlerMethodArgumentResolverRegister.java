package my.jwds.springweb.parse.method;

import org.springframework.core.MethodParameter;

import java.util.List;

/**
 * 处理方法参数解析注册器，自定义参数解析器后向spring注册bean，实现Ordered接口保证优先级
 */
public interface HandlerMethodArgumentResolverRegister {


    /**
     * 注册单个参数解析器
     * @param argumentResolver
     */
    void registerResolver(HandlerMethodArgumentResolver argumentResolver);


    /**
     * 注册参数解析器列表
     * @param argumentResolvers
     */
    void registerResolvers(List<HandlerMethodArgumentResolver> argumentResolvers);


    /**
     * 查找参数解析器
     * @param parameter
     * @return
     */
    HandlerMethodArgumentResolver findArgumentResolver(MethodParameter parameter);
}
