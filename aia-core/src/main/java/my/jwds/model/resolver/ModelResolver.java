package my.jwds.model.resolver;

import my.jwds.model.ModelProperty;

import java.lang.reflect.Type;

public interface ModelResolver<T> {



    ModelProperty resolve(T t);
}
