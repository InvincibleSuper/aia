package my.jwds.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodUtils {


    public static String getQualifiedName(Method method){
        Class clz = method.getDeclaringClass();
        StringBuilder res = new StringBuilder();
        res.append(clz.getTypeName());
        res.append(".");
        res.append(method.getName());
        res.append("(");
        for (int i = 0; i < method.getParameterCount(); i++) {
            Parameter parameter = method.getParameters()[i];
            res.append(parameter.getType().getTypeName());
            res.append(",");
        }
        return res.substring(0,res.length()-1)+")";
    }



}
