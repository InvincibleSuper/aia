package my.jwds.definition.resolver;

import com.sun.javadoc.*;
import my.jwds.DocTest;
import my.jwds.definition.AiaDefinitionException;
import my.jwds.definition.ClassDefinition;
import my.jwds.definition.MethodDefinition;
import my.jwds.definition.PropertyDefinition;
import my.jwds.utils.ClassUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JavadocDefinitionResolver extends AbstractDefinitionResolver{


    private String srcPath;

    private static HashMap<Class, ClassDoc> docs = new HashMap<>();

    public JavadocDefinitionResolver(String srcPath){
        this.srcPath = srcPath;
    }
    public static  class Doclet {

        public static boolean start(RootDoc root) {
            ClassDoc doc = root.classes()[0];
            try {
                JavadocDefinitionResolver.docs.put(ClassUtil.loadClass(doc.toString()),doc);
            } catch (ClassNotFoundException e) {
                throw new AiaDefinitionException("不能找到类"+doc);
            }
            return true;
        }
    }





    @Override
    protected void doInitDefinition(Class clz) {
        String fullyName = clz.getTypeName();
        String javaPath = srcPath+"/"+fullyName.substring(0,fullyName.indexOf("$")).replace('.','/')+".java";
        File file = new File(javaPath);
        ClassDefinition classDefinition = new ClassDefinition();
        classDefinition.setClz(clz);
        if (file.exists()){
            com.sun.tools.javadoc.Main.execute(new String[] {
                    "-doclet",
                    JavadocDefinitionResolver.Doclet.class.getName(),
                    "-encoding","utf-8",
                    javaPath});
            ClassDoc classDoc =  docs.get(clz);
            resolveNowAndInnerClassDefinition(clz,classDoc);
        }
    }

    private void resolveNowAndInnerClassDefinition(Class now,ClassDoc classDoc){
        Map<Field,PropertyDefinition> propertyDefinitionMap = getNowAndSuperPropertyDefinition(now,classDoc);
        Map<Method,MethodDefinition> methodDefinitionMap = getNowAndSuperMethodDefinition(now,classDoc);

        ClassDefinition classDefinition = new ClassDefinition(now,methodDefinitionMap,propertyDefinitionMap,classDoc.commentText());
        cache.put(now,classDefinition);
        ClassDoc[] inners = classDoc.innerClasses(false);
        String packageName = now.getPackage().getName();
        for (ClassDoc doc : inners) {
            try {
                Class clz = ClassUtil.loadClass(packageName + "."+ doc.typeName().replace('.','$'));
                resolveNowAndInnerClassDefinition(clz,doc);
            } catch (ClassNotFoundException e) {

            }
        }
    }



    private Map<Field,PropertyDefinition> getNowAndSuperPropertyDefinition(Class now,ClassDoc classDoc){
        Map<String, PropertyDefinition> propertyDefinitionMap = new LinkedHashMap<>();
        while(now != Object.class){
            for (FieldDoc fieldDoc : classDoc.fields(false)) {
                try {
                    if (propertyDefinitionMap.containsKey(fieldDoc.name()))continue;
                    Field field = now.getDeclaredField(fieldDoc.name());
                    if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())){
                        propertyDefinitionMap.put(fieldDoc.name(),new PropertyDefinition(field,fieldDoc.commentText()));
                    }
                } catch (NoSuchFieldException e) { }
            }
            now = now.getSuperclass();
            classDoc = classDoc.superclass();
        }
        Map<Field,PropertyDefinition> res = new LinkedHashMap<>();
        for (PropertyDefinition value : propertyDefinitionMap.values()) {
            res.put(value.getField(),value);
        }
        return res;
    }



    private Map<Method,MethodDefinition> getNowAndSuperMethodDefinition(Class now,ClassDoc classDoc){
        Map<String, MethodDefinition> methodDefinitionMap = new LinkedHashMap<>();
        while(now != Object.class){
            for (MethodDoc methodDoc : classDoc.methods(false)) {
                try {
                    String key = methodDoc.toString();
                    if (methodDefinitionMap.containsKey(key))continue;
                    Class [] methodParameter = new Class[methodDoc.parameters().length];
                    for (int i = 0; i < methodDoc.parameters().length; i++) {
                        methodParameter[i] = ClassUtil.loadClass(methodDoc.parameters()[i].type().asParameterizedType().toString());
                    }
                    Method method = now.getDeclaredMethod(methodDoc.name(),methodParameter);
                    MethodDefinition methodDefinition = new MethodDefinition(method,methodDoc.commentText());
                    methodDefinitionMap.put(key,methodDefinition);
                    Tag [] returnTag = methodDoc.tags("return");
                    String returnText = "";
                    for (Tag tag : returnTag) {
                        returnText += tag.text();
                    }
                    Map<String,String> parameterDefinitionMap = new HashMap<>();
                    for (ParamTag paramTag : methodDoc.paramTags()) {
                        parameterDefinitionMap.put(paramTag.name(),paramTag.text());
                    }
                    methodDefinition.setReturnDefinition(returnText);
                    Parameter[] parameters = method.getParameters();
                    String[] parameterDefinitions = new String[parameters.length];
                    Arrays.fill(parameterDefinitions,"");
                    for (int i = 0; i < parameters.length; i++) {
                        if (parameterDefinitionMap.containsKey(parameters[i].getName())){
                            parameterDefinitions[i] = parameterDefinitionMap.get(parameters[i].getName());
                        }
                    }
                    methodDefinition.setParameterDefinition(parameterDefinitions);
                } catch (NoSuchMethodException | ClassNotFoundException e) { }
            }
            now = now.getSuperclass();
            classDoc = classDoc.superclass();
        }
        Map<Method,MethodDefinition> res = new LinkedHashMap<>();
        for (MethodDefinition methodDefinition : methodDefinitionMap.values()) {
            res.put(methodDefinition.getMethod(),methodDefinition);
        }
        return res;
    }


}
