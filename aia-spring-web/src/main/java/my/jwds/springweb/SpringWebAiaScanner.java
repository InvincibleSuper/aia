package my.jwds.springweb;

import my.jwds.core.AiaApiScanner;
import my.jwds.core.AiaManager;
import my.jwds.definition.ClassDefinition;
import my.jwds.definition.MethodDefinition;
import my.jwds.definition.resolver.DefinitionResolver;
import my.jwds.model.ModelPropertyResolveInfo;
import my.jwds.model.resolver.ModelResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;

public class SpringWebAiaScanner implements AiaApiScanner, ApplicationContextAware {


    private ApplicationContext ac;

    private AiaManager aiaManager;

    private DefinitionResolver definitionResolver;

    private ModelResolver<ModelPropertyResolveInfo> modelResolver;


    @Override
    public void startScanner(AiaManager manager) {
        HashSet<Class>[] processControllers = getControllers();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }


    private HashSet<Class>[] getControllers(){
        HashSet<Class>[] res = new HashSet[]{new HashSet<>(),new HashSet<>()};
        Map<String,Object> restController = ac.getBeansWithAnnotation(RestController.class);
        for (Object o : restController.values()) {
            res[0].add(o.getClass());
        }
        Map<String,Object> controller = ac.getBeansWithAnnotation(Controller.class);
        for (Object o : controller.values()) {
            Class clz = o.getClass();
            if (clz.getAnnotation(ResponseBody.class) == null){
                res[1].add(clz);
            }else{
                res[0].add(clz);
            }
        }
        return res;
    }


    private void processController(Class clz,String returnType,AiaManager manager){
        ClassDefinition classDefinition =  definitionResolver.resolveClass(clz);
        RequestMapping globalRequestMapping = (RequestMapping) clz.getAnnotation(RequestMapping.class);
        String globalUrl = globalRequestMapping != null ? globalRequestMapping.name():"";
        Map<Method, MethodDefinition> methods = classDefinition.getMethodDefinitions();
        for (Method method : methods.keySet()) {
            AnnotationUtils.getAnnotation(method,RequestMapping.class);
        }
    }
}
