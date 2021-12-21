package my.jwds.utils;


import my.jwds.core.model.ArrayModelProperty;
import my.jwds.core.model.ModelProperty;
import my.jwds.core.model.ObjectModelProperty;
import my.jwds.core.model.SimpleModelProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型工具类
 */
public class ModelUtils {

    /**
     * 转换为 Map类的数据格式，简单类型直接返回默认值
     * @param modelProperty 模型
     * @return
     */
    public static Object toMap(ModelProperty modelProperty){
        if (modelProperty == null)return null;
        if (modelProperty instanceof SimpleModelProperty){
            return ((SimpleModelProperty) modelProperty).getValue();
        }else if (modelProperty instanceof ObjectModelProperty){
            ObjectModelProperty thisModel = (ObjectModelProperty) modelProperty;
            Map<String,Object> res = new LinkedHashMap<>();
            if (thisModel.getModel() != null){
                Map<String,Object> map = new LinkedHashMap<>();
                if (thisModel.getModel().getProperties() == null){
                    map.put(thisModel.getName(),null);
                }else{
                    for (ModelProperty property : thisModel.getModel().getProperties()) {
                        map.put(property.getName(),toMap(property));
                    }
                }

                res.putAll(map);
            }else{
                res.put("String",toMap(thisModel.getContainerContent()[1]));
            }
            return res;
        }else{
            ArrayModelProperty thisModel = (ArrayModelProperty) modelProperty;
            List list = new ArrayList();
            list.add(toMap(thisModel.getComponent()));
            return list;
        }
    }
}
