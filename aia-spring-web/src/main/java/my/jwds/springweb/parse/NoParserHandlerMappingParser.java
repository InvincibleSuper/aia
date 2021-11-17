package my.jwds.springweb.parse;

import my.jwds.core.AiaManager;
import org.springframework.web.servlet.HandlerMapping;

/**
 * HandlerMapping找不到解析器时返回
 * 避免返回 null
 */
public class NoParserHandlerMappingParser extends AbstractHandlerMappingParser{

    /**
     * 返回支持处理类
     *
     * @return 支持的处理类
     */
    @Override
    public Class supportClass() {
        return HandlerMapping.class;
    }

    /**
     * 解析 HandlerMapping
     *
     * @param aiaManager     总管理器
     * @param handlerMapping spring请求处理映射
     */
    @Override
    public void parse(AiaManager aiaManager, HandlerMapping handlerMapping) {

    }
}