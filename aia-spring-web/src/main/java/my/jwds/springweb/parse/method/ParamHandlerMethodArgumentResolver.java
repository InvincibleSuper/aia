package my.jwds.springweb.parse.method;

import my.jwds.core.api.InvokeContentType;
import my.jwds.core.api.InvokeParam;
import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.model.ModelProperty;
import my.jwds.core.model.ModelPropertyResolveInfo;
import my.jwds.core.model.resolver.ModelResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 处理方法参数解析类，解析为param类型的参数，默认的兜底解析器
 * 其他解析器可继承此类，重写部分方法实现灵活配置
 */
public class ParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {



    private DefinitionResolver definitionResolver;

    private ModelResolver modelResolver;

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public ParamHandlerMethodArgumentResolver(DefinitionResolver definitionResolver, ModelResolver modelResolver) {
        this.definitionResolver = definitionResolver;
        this.modelResolver = modelResolver;
    }

    /**
     * 能解析吗
     *
     * @param parameter 方法参数
     * @return 结果
     */
    @Override
    public boolean canResolve(MethodParameter parameter) {
        return parameter != null;
    }

    /**
     * 解析内容类型
     * @param parameter 方法参数
     * @return 参数类别
     */
    protected InvokeContentType resolveContentType(MethodParameter parameter){
        return InvokeContentType.param;
    }


    /**
     * 解析方法参数注释
     * @param parameter 方法参数
     * @return 方法参数注释
     */
    protected String resolveDefinition(MethodParameter parameter){
        return definitionResolver.resolveMethodPropertiesDefinition(parameter.getMethod(),parameter.getParameter());
    }

    /**
     * 解析方法参数注释
     * @param parameter 方法参数
     * @return 方法参数注释
     */
    protected String resolveArgumentName(MethodParameter parameter){
        parameter.initParameterNameDiscovery(parameterNameDiscoverer);
        return parameter.getParameterName();
    }


    /**
     * 解析方法参数模型
     * @param parameter 方法参数
     * @return 方法参数模型
     */
    protected ModelProperty resolveModel(MethodParameter parameter){
        ModelPropertyResolveInfo resolveInfo = new ModelPropertyResolveInfo();
        resolveInfo.setType(parameter.getGenericParameterType());
        resolveInfo.setName(resolveArgumentName(parameter));
        return modelResolver.resolve(resolveInfo);
    }


    /**
     * 解析方法参数前缀
     * @param parameter 方法参数
     * @return 方法参数前缀
     */
    protected String resolvePrefix(MethodParameter parameter){
        return null;
    }

    /**
     * 解析一个请求方法的参数
     *
     * @param parameter 方法参数
     * @return http执行参数
     */
    @Override
    public InvokeParam resolve(MethodParameter parameter) {
        InvokeParam invokeParam = new InvokeParam();
        invokeParam.setContentType(resolveContentType(parameter));
        invokeParam.setDefinition(resolveDefinition(parameter));
        invokeParam.setModel(resolveModel(parameter));
        invokeParam.setPrefix(resolvePrefix(parameter));
        invokeParam.setName(resolveArgumentName(parameter));
        return invokeParam;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
