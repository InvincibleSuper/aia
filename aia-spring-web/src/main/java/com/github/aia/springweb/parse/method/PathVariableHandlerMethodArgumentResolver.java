package com.github.aia.springweb.parse.method;

import com.github.aia.core.api.InvokeContentType;
import com.github.aia.core.api.definition.resolver.DefinitionResolver;
import com.github.aia.core.model.resolver.ModelResolver;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;

public class PathVariableHandlerMethodArgumentResolver extends ParamHandlerMethodArgumentResolver{


    public PathVariableHandlerMethodArgumentResolver(DefinitionResolver definitionResolver, ModelResolver modelResolver) {
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
        return parameter.getParameterAnnotation(PathVariable.class) != null;
    }

    /**
     * 解析内容类型
     * @param parameter 方法参数
     * @return 参数类别
     */
    protected InvokeContentType resolveContentType(MethodParameter parameter){
        return InvokeContentType.url;
    }


    /**
     * 解析方法参数注释
     * @param parameter 方法参数
     * @return 方法参数注释
     */
    protected String resolveArgumentName(MethodParameter parameter){
        PathVariable pathVariable = parameter.getParameterAnnotation(PathVariable.class);
        return pathVariable.name();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
