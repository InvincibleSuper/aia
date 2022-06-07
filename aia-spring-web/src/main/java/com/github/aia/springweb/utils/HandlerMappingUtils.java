package com.github.aia.springweb.utils;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Field;
import java.util.List;

public class HandlerMappingUtils {


    public static List<HandlerMapping> get(ApplicationContext ac){
        DispatcherServlet dispatcherServlet = BeanFactoryUtils.beanOfType(ac, DispatcherServlet.class);

        while(true){
            try {
                Field handleMappingsField = dispatcherServlet.getClass().getDeclaredField("handlerMappings");
                handleMappingsField.setAccessible(true);
                List<HandlerMapping> handlerMappings = (List<HandlerMapping>) handleMappingsField.get(dispatcherServlet);
                if (handlerMappings == null){
                    Thread.sleep(100);
                }else{
                    return handlerMappings;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
