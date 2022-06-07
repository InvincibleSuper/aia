package com.github.aia.springweb.parse.method;

import org.springframework.core.MethodParameter;
import org.springframework.core.OrderComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 默认的方法参数解析器注册器
 * 使用ArrayList列表存储
 * 单个注册每次都会对原列表进行排序
 */
public class DefaultHandlerMethodArgumentResolverRegister implements HandlerMethodArgumentResolverRegister{


    private List<HandlerMethodArgumentResolver> registerContents = new ArrayList<>();


    private Comparator comparator = new OrderComparator();


    /**
     * 注册单个参数解析器
     *
     * @param argumentResolver
     */
    @Override
    public void registerResolver(HandlerMethodArgumentResolver argumentResolver) {
        registerContents.add(argumentResolver);
        registerContents.sort(comparator);
    }

    /**
     * 注册参数解析器列表
     *
     * @param argumentResolvers
     */
    @Override
    public void registerResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        registerContents.addAll(argumentResolvers);
        registerContents.sort(comparator);
    }

    /**
     * 查找参数解析器
     *
     * @param parameter
     * @return
     */
    @Override
    public HandlerMethodArgumentResolver findArgumentResolver(MethodParameter parameter) {
        for (HandlerMethodArgumentResolver handlerMethodArgumentResolver : registerContents) {
            if (handlerMethodArgumentResolver.canResolve(parameter)){
                return handlerMethodArgumentResolver;
            }
        }
        return null;
    }
}
