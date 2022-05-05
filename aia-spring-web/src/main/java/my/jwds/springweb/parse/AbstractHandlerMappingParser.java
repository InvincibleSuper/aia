package my.jwds.springweb.parse;

import my.jwds.core.AiaContext;
import org.springframework.web.servlet.HandlerMapping;


/**
 * 抽象的spring处理器映射解析器<br/>
 * 子类继承{@link AbstractHandlerMappingParser#supportClass()}返回支持的HandleMapping<br/>
 * 解析委托到子类的{@link AbstractHandlerMappingParser#onParse(AiaContext, HandlerMapping)};
 */
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
     * @param aiaContext     总管理器
     * @param handlerMapping spring请求处理映射
     */
    @Override
    public void parse(AiaContext aiaContext, HandlerMapping handlerMapping) {
        if (!support(handlerMapping)){
            throw new ClassCastException("不支持此HandleMapping，预期的类型是"+ supportClass());
        }
        onParse(aiaContext,handlerMapping);
    }

    /**
     * 委托给子类处理
     * @param aiaContext 总管理器
     * @param handlerMapping spring请求处理映射
     */
    protected abstract void onParse(AiaContext aiaContext,HandlerMapping handlerMapping);


}
