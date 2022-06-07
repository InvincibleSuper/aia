package com.github.aia.core.api;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DefaultApiComparator implements Comparator<InvokeApi> {


    private Map<String,Integer> methodSizeMap = new HashMap<>();

    public DefaultApiComparator(){
        methodSizeMap.put("GET",1);
        methodSizeMap.put("HEAD",2);
        methodSizeMap.put("POST",3);
        methodSizeMap.put("PUT",4);
        methodSizeMap.put("PATCH",5);
        methodSizeMap.put("DELETE",6);
        methodSizeMap.put("OPTIONS",7);
        methodSizeMap.put("TRACE",8);
        methodSizeMap.put(null,9);

    }



    /**
     * 使用api的method 和 url进行比对，method的大小在对比前就已经固定完毕
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(InvokeApi o1, InvokeApi o2) {
        int method1 = methodSizeMap.get(o1.getUrl().getMethod().toUpperCase());
        int method2 = methodSizeMap.get(o2.getUrl().getMethod().toUpperCase());
        if (method1 == method2){
            return o1.getUrl().getUrl().compareTo(o2.getUrl().getUrl());
        }
        return method1 - method2;
    }
}
