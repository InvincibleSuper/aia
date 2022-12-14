package com.github.aia.springweb.parse.method;

import com.github.aia.core.api.InvokeParam;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;

/**
 * handlerMethod 属性解析
 * 解析出Spring处理方法的属性,生成执行参数
 * @see InvokeParam
 */
public interface HandlerMethodArgumentResolver extends Ordered {

    /**
     * 能解析吗
     * @param parameter 方法参数
     * @return 结果
     */
    boolean canResolve(MethodParameter parameter);


    /**
     * 解析一个请求方法的参数
     * @param parameter 方法参数
     * @return http执行参数
     */
    InvokeParam resolve(MethodParameter parameter);



}
