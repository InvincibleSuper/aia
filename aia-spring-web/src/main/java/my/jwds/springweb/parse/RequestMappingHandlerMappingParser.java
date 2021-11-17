package my.jwds.springweb.parse;

import my.jwds.core.AiaManager;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;



public class RequestMappingHandlerMappingParser extends AbstractHandlerMappingParser {




    /**
     * 返回支持处理类
     *
     * @return 支持的处理类
     */
    @Override
    public Class supportClass() {
        return  RequestMappingHandlerMapping.class;
    }

    /**
     * 解析 HandlerMapping
     *
     * @param aiaManager     总管理器
     * @param handlerMapping spring请求处理映射
     */
    @Override
    public void parse(AiaManager aiaManager, HandlerMapping handlerMapping) {
        if (!support(handlerMapping)){
            throw new ClassCastException("不支持此HandleMapping，预期的类型是"+RequestMappingHandlerMapping.class);
        }
    }

}
