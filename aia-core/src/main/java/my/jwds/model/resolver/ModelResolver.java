package my.jwds.model.resolver;

import my.jwds.model.ModelProperty;

import java.lang.reflect.Type;

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
