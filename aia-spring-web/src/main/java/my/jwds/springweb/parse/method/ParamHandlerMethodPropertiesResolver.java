package my.jwds.springweb.parse.method;

import my.jwds.api.InvokeContentType;
import my.jwds.api.InvokeParam;
import my.jwds.api.definition.resolver.DefinitionResolver;
import my.jwds.model.ModelProperty;
import my.jwds.model.resolver.ModelResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 保底类，默认的兜底解析器
 * 其他解析器可继承此类，重写部分方法实现灵活配置
 */
public class ParamHandlerMethodPropertiesResolver implements HandlerMethodPropertiesResolver{



    private DefinitionResolver definitionResolver;


    private ModelResolver modelResolver;




    /**
     * 能解析吗
     *
     * @param parameter 方法参数
     * @return 结果
     */
    @Override
    public boolean canResolve(Parameter parameter) {
        return parameter != null;
    }

    /**
     * 解析内容类型
     * @param method 方法
     * @param parameter 方法参数
     * @return 参数类别
     */
    protected  InvokeContentType resolveContentType(Method method,Parameter parameter){
        return InvokeContentType.param;
    }


    /**
     * 解析方法参数注释
     * @param method 方法
     * @param parameter 方法参数
     * @return 方法参数注释
     */
    protected String resolveDefinition(Method method,Parameter parameter){
        return definitionResolver.resolveMethodPropertiesDefinition(method,parameter);
    }



    /**
     * 解析方法参数模型
     * @param method 方法
     * @param parameter 方法参数
     * @return 方法参数模型
     */
    protected ModelProperty resolveModel(Method method,Parameter parameter){
        return modelResolver.resolve(parameter.getParameterizedType());
    }


    /**
     * 解析方法参数前缀
     * @param method 方法
     * @param parameter 方法参数
     * @return 方法参数前缀
     */
    protected String resolvePrefix(Method method,Parameter parameter){
        return null;
    }

    /**
     * 解析一个请求方法的参数
     *
     * @param method 方法
     * @param parameter 方法参数
     * @return http执行参数
     */
    @Override
    public InvokeParam resolve(Method method,Parameter parameter) {
        InvokeParam invokeParam = new InvokeParam();
        invokeParam.setContentType(resolveContentType(method,parameter).name());
        invokeParam.setDefinition(resolveDefinition(method,parameter));
        invokeParam.setModel(resolveModel(method,parameter));
        invokeParam.setPrefix(resolvePrefix(method,parameter));
        return invokeParam;
    }

    /**
     * 解析整个请求映射方法
     *
     * @param method 请求映射方法
     * @return http执行参数列表
     */
    @Override
    public List<InvokeParam> resolveMethod(Method method) {
        List<InvokeParam> res = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            if (canResolve(parameter)){
                res.add(resolve(method,parameter));
            }
        }
        return res;
    }
}
