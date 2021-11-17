package my.jwds.springweb.parse;

import org.springframework.web.servlet.HandlerMapping;

/**
 * Spring HandlerMapping 解析器注册接口
 */
public interface SpringHandlerMappingParserRegister {

    /**
     * 注册解析器
     * @param parser 解析器
     */
    void registerParser(SpringHandlerMappingParser parser);


    /**
     * 查找HandlerMapping解析器
     * @param handlerMapping spring请求处理映射
     * @return 解析器
     */
    SpringHandlerMappingParser findParser(HandlerMapping handlerMapping);

}
