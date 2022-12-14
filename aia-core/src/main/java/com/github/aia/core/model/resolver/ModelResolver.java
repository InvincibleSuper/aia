package com.github.aia.core.model.resolver;

import com.github.aia.core.model.ModelProperty;

/**
 * 模型解析器
 * @param <T>
 */
public interface ModelResolver<T> {


    /**
     * 解析
     * @param t 参数
     * @return 模型属性
     */
    ModelProperty resolve(T t);



}
