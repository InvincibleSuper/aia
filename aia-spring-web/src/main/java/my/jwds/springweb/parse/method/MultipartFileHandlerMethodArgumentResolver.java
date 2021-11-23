package my.jwds.springweb.parse.method;

import my.jwds.api.InvokeContentType;
import my.jwds.api.definition.resolver.DefinitionResolver;
import my.jwds.model.resolver.ModelResolver;
import org.springframework.core.MethodParameter;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MultipartFileHandlerMethodArgumentResolver extends ParamHandlerMethodArgumentResolver {

    public MultipartFileHandlerMethodArgumentResolver(DefinitionResolver definitionResolver, ModelResolver modelResolver) {
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
        return MultipartResolutionDelegate.isMultipartArgument(parameter);
    }

    /**
     * 解析内容类型
     * @param parameter 方法参数
     * @return 参数类别
     */
    protected InvokeContentType resolveContentType(MethodParameter parameter){
        return InvokeContentType.file;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
