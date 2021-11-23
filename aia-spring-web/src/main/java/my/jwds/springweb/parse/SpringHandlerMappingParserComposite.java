package my.jwds.springweb.parse;

import my.jwds.core.AiaManager;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;


/**
 * spring 方法解析器组合
 */
public class SpringHandlerMappingParserComposite implements SpringHandlerMappingParser{



    /**
     * 解析器列表
     */
    private List<SpringHandlerMappingParser> parsers = new ArrayList<>();

    /**
     * 没有找到解析器默认返回
     */
    private SpringHandlerMappingParser defaultParser = new NoParserHandlerMappingParser();



    public SpringHandlerMappingParserComposite(){

    }

    public void addParser(SpringHandlerMappingParser parser){
        parsers.add(parser);
    }

    public void addParsers(List<SpringHandlerMappingParser> parsers){
        this.parsers.addAll(parsers);
    }

    public void setDefaultParser(SpringHandlerMappingParser parser){
        this.defaultParser = parser;
    }


    /**
     * 是否支持此 HandlerMapping
     *
     * @param handlerMapping spring请求处理映射
     * @return 是|否
     */
    @Override
    public boolean support(HandlerMapping handlerMapping) {
        return false;
    }


    /**
     * 解析 HandlerMapping
     *
     * @param aiaManager     总管理器
     * @param handlerMapping spring请求处理映射
     */
    @Override
    public void parse(AiaManager aiaManager, HandlerMapping handlerMapping) {
        for (SpringHandlerMappingParser parser : parsers) {
            if (parser.support(handlerMapping)){
                parser.parse(aiaManager,handlerMapping);
            }
        }
    }
}
