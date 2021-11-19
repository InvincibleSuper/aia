package my.jwds.springweb.parse.method;

import my.jwds.api.InvokeParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * handlerMethod 属性解析
 * 解析出Spring处理方法的属性,生成执行参数
 * @see my.jwds.api.InvokeParam
 */
public interface HandlerMethodPropertiesResolver {

    /**
     * 能解析吗
     * @param parameter 方法参数
     * @return 结果
     */
    boolean canResolve(Parameter parameter);


    /**
     * 解析一个请求方法的参数
     * @param method 方法
     * @param parameter 方法参数
     * @return http执行参数
     */
    InvokeParam resolve(Method method,Parameter parameter);


    /**
     * 解析整个请求映射方法
     * @param method 请求映射方法
     * @return http执行参数列表
     */
    List<InvokeParam> resolveMethod(Method method);
}
