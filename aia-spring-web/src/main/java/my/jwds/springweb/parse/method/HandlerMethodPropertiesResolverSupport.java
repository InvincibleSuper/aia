package my.jwds.springweb.parse.method;

import my.jwds.api.InvokeParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持所有method的解析，内部维护一个HandlerMethodPropertiesResolver列表
 * 对传入属性进行分配到合适的HandlerMethodPropertiesResolver解析
 * 当没有找到解析器，使用保底解析器解析
 */
public class HandlerMethodPropertiesResolverSupport implements HandlerMethodPropertiesResolver {


    private List<HandlerMethodPropertiesResolver> resolvers;


    private HandlerMethodPropertiesResolver earsResolver;


    public void addResolver(HandlerMethodPropertiesResolver resolver){
        this.resolvers.add(resolver);
    }

    public void setEarsResolver(HandlerMethodPropertiesResolver earsResolver) {
        this.earsResolver = earsResolver;
    }

    /**
     * 能解析吗
     *
     * @param parameter 方法参数
     * @return 结果
     */
    @Override
    public boolean canResolve(Parameter parameter) {
        for (HandlerMethodPropertiesResolver resolver : resolvers) {
            if (resolver.canResolve(parameter))return true;
        }
        return false;
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
        for (HandlerMethodPropertiesResolver resolver : resolvers) {
            if (resolver.canResolve(parameter))return resolver.resolve(method,parameter);
        }
        if (earsResolver != null){
            return earsResolver.resolve(method,parameter);
        }
        return null;
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
            res.add(resolve(method,parameter));
        }
        return res;
    }
}
