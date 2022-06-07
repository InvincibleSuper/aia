package com.github.aia.core.model;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class JavaTypeModelTable {

    public static final Map<Class,Object> BASE_MODEL = new HashMap<>(256);
    public static final ObjectModelProperty OBJECT_MODEL_PROPERTY = new ObjectModelProperty("object","object",Object.class,null,new Model());
    public static final Map<Class,String> JAVA_TYPE_MAP = new LinkedHashMap<>(256);
    static{
        Date NOW = new Date();
        String NOW_TIME = new SimpleDateFormat("hh:mm:ss").format(NOW);
        String NOW_DATE = new SimpleDateFormat("yyyy-MM-dd").format(NOW);
        String NOW_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(NOW);
        BASE_MODEL.put(byte.class,new NumberModelProperty(null,"byte",byte.class,0));
        BASE_MODEL.put(char.class,new StringModelProperty(null,"char",char.class,"A"));
        BASE_MODEL.put(boolean.class,new StringModelProperty(null,"boolean",boolean.class,"false"));
        BASE_MODEL.put(short.class,new NumberModelProperty(null,"int16",short.class,0));
        BASE_MODEL.put(int.class,new NumberModelProperty(null,"int32",int.class,0));
        BASE_MODEL.put(float.class,new NumberModelProperty(null,"float",float.class,1.0));
        BASE_MODEL.put(double.class,new NumberModelProperty(null,"double",double.class,1.00 ));
        BASE_MODEL.put(long.class,new NumberModelProperty(null,"int64",long.class,0 ));
        BASE_MODEL.put(String.class,new StringModelProperty(null,"string",String.class,"String" ));
        BASE_MODEL.put(Character.class,new StringModelProperty(null,"char",Character.class,"A" ));
        BASE_MODEL.put(Boolean.class,new StringModelProperty(null,"boolean",Boolean.class,"false" ));
        LinkedHashMap<Class,Object> number = new LinkedHashMap<>();
        number.put(Byte.class,new NumberModelProperty(null,"byte",Byte.class,0 ));
        number.put(Short.class,new NumberModelProperty(null,"int16",Short.class,0 ));
        number.put(Integer.class,new NumberModelProperty(null,"int32",Integer.class,0));
        number.put(Float.class,new NumberModelProperty(null,"float",Float.class,0.0 ));
        number.put(Double.class,new NumberModelProperty(null,"double",Double.class,0.00 ));
        number.put(Long.class,new NumberModelProperty(null,"int64",Long.class,0 ));
        number.put(Number.class,new NumberModelProperty(null,"int64",Number.class,0 ));
        BASE_MODEL.put(Number.class,number);
        BASE_MODEL.put(Date.class,new StringModelProperty(null,"date-time",Date.class, NOW_DATE_TIME ));
        BASE_MODEL.put(LocalDate.class,new StringModelProperty(null,"date",LocalDate.class, NOW_DATE ));
        BASE_MODEL.put(LocalTime.class,new StringModelProperty(null,"time",LocalTime.class, NOW_TIME ));
        BASE_MODEL.put(LocalDateTime.class,new StringModelProperty(null,"datetime",LocalDateTime.class, NOW_DATE_TIME ));
        ModelProperty key = ((ModelProperty) BASE_MODEL.get(String.class)).clone();
        key.setName("key");
        ModelProperty value = OBJECT_MODEL_PROPERTY.clone();
        value.setName("value");
        BASE_MODEL.put(Map.class,new ObjectModelProperty(null,"object",Map.class,new ModelProperty[]{key,value},null));
        BASE_MODEL.put(Collection.class,new ArrayModelProperty(null,"array",Collection.class));
    }


    public static ModelProperty resolveSpecial(Class clz, Type type, String name){
        if (clz.isArray()){
            return new ArrayModelProperty(name,clz);
        }else if (clz == Object.class){
            ObjectModelProperty objectModelProperty = OBJECT_MODEL_PROPERTY.clone();
            objectModelProperty.setName(name);
            return objectModelProperty;
        }
        Object o = BASE_MODEL;
        while(o != null){
            if (o instanceof Map){
                Map<Class,Object> searchMap = (Map<Class, Object>) o;
                o = searchMap.get(clz);
                if (o == null){
                    for (Class aClass : searchMap.keySet()) {
                        if (aClass.isAssignableFrom(clz)){
                            o = searchMap.get(aClass);
                            break;
                        }
                    }
                }
            }else{
                ModelProperty template =  ((ModelProperty)o);
                ModelProperty res = template.clone();
                res.setName(name);
                return res;
            }

        }
        ModelProperty obj = OBJECT_MODEL_PROPERTY.clone();
        obj.setJavaType(clz);
        obj.setName(name);
        return obj ;
    }

    public static void initJavaTypeMap(){
        JAVA_TYPE_MAP.put(byte.class,"byte");
        JAVA_TYPE_MAP.put(char.class,"char");
        JAVA_TYPE_MAP.put(boolean.class,"boolean");
        JAVA_TYPE_MAP.put(short.class,"int16");
        JAVA_TYPE_MAP.put(int.class,"int32");
        JAVA_TYPE_MAP.put(float.class,"float");
        JAVA_TYPE_MAP.put(double.class,"double");
        JAVA_TYPE_MAP.put(long.class,"int64");
        JAVA_TYPE_MAP.put(String.class,"string");
        JAVA_TYPE_MAP.put(Character.class,"char");
        JAVA_TYPE_MAP.put(Boolean.class,"boolean");
        JAVA_TYPE_MAP.put(Byte.class,"byte");
        JAVA_TYPE_MAP.put(Short.class,"int16");
        JAVA_TYPE_MAP.put(Integer.class,"int32");
        JAVA_TYPE_MAP.put(Float.class,"float");
        JAVA_TYPE_MAP.put(Double.class,"double");
        JAVA_TYPE_MAP.put(Long.class,"int64");
        JAVA_TYPE_MAP.put(Number.class,"int64");
        JAVA_TYPE_MAP.put(Date.class,"date");
        JAVA_TYPE_MAP.put(LocalDate.class,"date");
        JAVA_TYPE_MAP.put(LocalTime.class,"time");
        JAVA_TYPE_MAP.put(LocalDateTime.class,"datetime");
        JAVA_TYPE_MAP.put(Map.class,"obj");
    }




}
