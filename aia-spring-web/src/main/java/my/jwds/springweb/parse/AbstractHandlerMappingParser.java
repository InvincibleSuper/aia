package my.jwds.springweb.parse;

import org.springframework.web.servlet.HandlerMapping;

public abstract class AbstractHandlerMappingParser implements SpringHandlerMappingParser{
    /**
     * 是否支持此 HandlerMapping
     *
     * @param handlerMapping spring请求处理映射
     * @return 是|否
     */
    @Override
    public boolean support(HandlerMapping handlerMapping) {
        return supportClass().isAssignableFrom(handlerMapping.getClass());
    }
}
