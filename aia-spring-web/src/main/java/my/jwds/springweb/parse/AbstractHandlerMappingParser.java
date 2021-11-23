package my.jwds.springweb.parse;

import my.jwds.core.AiaManager;
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





    /**
     * 返回支持处理类
     * @return 支持的处理类
     */
    protected abstract Class supportClass();



    /**
     * 解析 HandlerMapping
     *
     * @param aiaManager     总管理器
     * @param handlerMapping spring请求处理映射
     */
    @Override
    public void parse(AiaManager aiaManager, HandlerMapping handlerMapping) {
        if (!support(handlerMapping)){
            throw new ClassCastException("不支持此HandleMapping，预期的类型是"+ supportClass());
        }
        onParse(aiaManager,handlerMapping);
    }

    /**
     * 委托给子类处理
     * @param aiaManager 总管理器
     * @param handlerMapping spring请求处理映射
     */
    protected abstract void onParse(AiaManager aiaManager,HandlerMapping handlerMapping);


}
