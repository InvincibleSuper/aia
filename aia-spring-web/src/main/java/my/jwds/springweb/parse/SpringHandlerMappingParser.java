package my.jwds.springweb.parse;

import my.jwds.core.AiaManager;
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
     * @param aiaManager 总管理器
     * @param handlerMapping spring请求处理映射
     */
    void parse(AiaManager aiaManager,HandlerMapping handlerMapping);

}
