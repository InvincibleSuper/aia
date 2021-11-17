package my.jwds.springweb.parse;

import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class SpringHandlerMappingParserRegisterSupport implements SpringHandlerMappingParserRegister{



    /**
     * 解析器列表
     */
    private List<SpringHandlerMappingParser> parsers = new ArrayList<>();

    /**
     * 没有找到解析器默认返回
     */
    private SpringHandlerMappingParser defaultParser = new NoParserHandlerMappingParser();



    public SpringHandlerMappingParserRegisterSupport(){
        parsers.add(new RequestMappingHandlerMappingParser());
    }

    /**
     * 注册解析器
     *
     * @param parser 解析器
     */
    @Override
    public void registerParser(SpringHandlerMappingParser parser) {
        parsers.add(parser);
    }

    /**
     * 查找HandlerMapping解析器
     *
     * @param handlerMapping spring请求处理映射
     * @return 解析器
     */
    @Override
    public SpringHandlerMappingParser findParser(HandlerMapping handlerMapping) {
        for (SpringHandlerMappingParser parser : parsers) {
            if (parser.support(handlerMapping))return parser;
        }
        return defaultParser;
    }
}
