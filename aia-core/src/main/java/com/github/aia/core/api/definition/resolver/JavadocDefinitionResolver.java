package com.github.aia.core.api.definition.resolver;

import com.sun.javadoc.*;
import com.github.aia.core.api.definition.ClassDefinition;
import com.github.aia.core.api.definition.MethodDefinition;
import com.github.aia.core.api.definition.FieldDefinition;
import com.github.aia.utils.ClassUtils;

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

    private static HashMap<String, ClassDoc> docs = new HashMap<>();

    public JavadocDefinitionResolver(String srcPath){
        this.srcPath = srcPath;
    }
    public static  class Doclet {

        public static boolean start(RootDoc root) {
            ClassDoc doc = root.classes()[0];
            JavadocDefinitionResolver.docs.put(doc.toString(),doc);
            return true;
        }
    }





    @Override
    protected void doInitDefinition(Class clz) {
        String fullyName = clz.getTypeName();
        int innerClassIndex = fullyName.indexOf("$");
        String javaPath = srcPath+"/"+fullyName.substring(0,innerClassIndex==-1?fullyName.length():innerClassIndex).replace('.','/')+".java";
        File file = new File(javaPath);
        if (file.exists()){
            com.sun.tools.javadoc.Main.execute(new String[] {
                    "-doclet",
                    JavadocDefinitionResolver.Doclet.class.getName(),
                    "-encoding","utf-8",
                    javaPath});
            ClassDoc classDoc =  docs.get(fullyName);
            resolveNowAndInnerClassDefinition(clz,classDoc);
        }
    }

    private void resolveNowAndInnerClassDefinition(Class now,ClassDoc classDoc){
        Map<Field, FieldDefinition> propertyDefinitionMap = getNowAndSuperPropertyDefinition(now,classDoc);
        Map<Method,MethodDefinition> methodDefinitionMap = getNowAndSuperMethodDefinition(now,classDoc);

        ClassDefinition classDefinition = new ClassDefinition(now,methodDefinitionMap,propertyDefinitionMap,classDoc.commentText());
        cache.put(now,classDefinition);
        ClassDoc[] inners = classDoc.innerClasses(false);
        String packageName = now.getPackage().getName();
        for (ClassDoc doc : inners) {
            try {
                Class clz = ClassUtils.loadClass(packageName + "."+ doc.typeName().replace('.','$'));
                resolveNowAndInnerClassDefinition(clz,doc);
            } catch (ClassNotFoundException e) {

            }
        }
    }



    private Map<Field, FieldDefinition> getNowAndSuperPropertyDefinition(Class now, ClassDoc classDoc){
        Map<String, FieldDefinition> propertyDefinitionMap = new LinkedHashMap<>();
        while(now != Object.class){
            for (FieldDoc fieldDoc : classDoc.fields(false)) {
                try {
                    if (propertyDefinitionMap.containsKey(fieldDoc.name()))continue;
                    Field field = now.getDeclaredField(fieldDoc.name());
                    if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())){
                        propertyDefinitionMap.put(fieldDoc.name(),new FieldDefinition(field,fieldDoc.commentText()));
                    }
                } catch (NoSuchFieldException e) { }
            }
            now = now.getSuperclass();
            classDoc = classDoc.superclass();
        }
        Map<Field, FieldDefinition> res = new LinkedHashMap<>();
        for (FieldDefinition value : propertyDefinitionMap.values()) {
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
                        methodParameter[i] = ClassUtils.loadClass(methodDoc.parameters()[i].type().toString());
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
