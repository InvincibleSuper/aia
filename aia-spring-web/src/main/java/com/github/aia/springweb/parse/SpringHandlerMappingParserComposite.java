package com.github.aia.springweb.parse;

import com.github.aia.core.AiaContext;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;


/**
 * Spring处理映射解析器组合，使用本身的处理映射解析器列表解析
 */
public class SpringHandlerMappingParserComposite implements SpringHandlerMappingParser{



    /**
     * 解析器列表
     */
    private List<SpringHandlerMappingParser> parsers = new ArrayList<>();




    public SpringHandlerMappingParserComposite(){

    }

    public void addParser(SpringHandlerMappingParser parser){
        parsers.add(parser);
    }

    public void addParsers(List<SpringHandlerMappingParser> parsers){
        this.parsers.addAll(parsers);
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
     * @param aiaContext     上下文
     * @param handlerMapping spring请求处理映射
     */
    @Override
    public void parse(AiaContext aiaContext, HandlerMapping handlerMapping) {
        for (SpringHandlerMappingParser parser : parsers) {
            if (parser.support(handlerMapping)){
                parser.parse(aiaContext,handlerMapping);
            }
        }
    }
}
