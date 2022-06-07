package com.github.aia.springweb.parse;

import com.github.aia.core.AiaContext;
import org.springframework.web.servlet.HandlerMapping;


/**
 * spring 处理器映射解析器
 */
public interface SpringHandlerMappingParser {

    /**
     * 是否支持此 HandlerMapping
     * @param handlerMapping spring请求处理映射
     * @return 是|否
     */
    boolean support(HandlerMapping handlerMapping);




    /**
     * 解析 HandlerMapping
     * @param aiaContext 上下文
     * @param handlerMapping spring请求处理映射
     */
    void parse(AiaContext aiaContext, HandlerMapping handlerMapping);

}
