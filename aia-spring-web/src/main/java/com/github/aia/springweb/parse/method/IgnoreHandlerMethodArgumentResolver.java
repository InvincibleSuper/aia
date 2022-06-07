package com.github.aia.springweb.parse.method;


import com.github.aia.core.api.InvokeParam;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;


/**
 * 忽略此属性
 */
public class IgnoreHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{


    /**
     * 能解析吗
     *
     * @param parameter 方法参数
     * @return 结果
     */
    @Override
    public boolean canResolve(MethodParameter parameter) {
        return Model.class.isAssignableFrom(parameter.getParameterType());
    }

    /**
     * 解析一个请求方法的参数
     *
     * @param parameter 方法参数
     * @return http执行参数
     */
    @Override
    public InvokeParam resolve(MethodParameter parameter) {
        return null;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
