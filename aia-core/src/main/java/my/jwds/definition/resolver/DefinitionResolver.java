package my.jwds.definition.resolver;

import my.jwds.definition.ClassDefinition;
import my.jwds.definition.MethodDefinition;
import my.jwds.definition.PropertyDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public interface DefinitionResolver {


    ClassDefinition resolveClass(Class clz);
    Map<Field,PropertyDefinition> resolveProperty(Class clz);
    Map<Method,MethodDefinition> resolveMethod(Class clz);
    Map<Class,ClassDefinition> getCache();
}
