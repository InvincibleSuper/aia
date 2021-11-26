package my.jwds.springweb.parse.method;

import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.model.resolver.ModelResolver;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;

public class RequestParamHandlerMethodArgumentResolver extends ParamHandlerMethodArgumentResolver{

    public RequestParamHandlerMethodArgumentResolver(DefinitionResolver definitionResolver, ModelResolver modelResolver) {
        super(definitionResolver, modelResolver);
    }





    /**
     * 能解析吗
     *
     * @param parameter 方法参数
     * @return 结果
     */
    @Override
    public boolean canResolve(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestParam.class)!= null;
    }

    /**
     * 解析方法参数注释
     * @param parameter 方法参数
     * @return 方法参数注释
     */
    @Override
    protected String resolveArgumentName(MethodParameter parameter){
        RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
        return requestParam.name();
    }





    @Override
    public int getOrder() {
        return 0;
    }
}
