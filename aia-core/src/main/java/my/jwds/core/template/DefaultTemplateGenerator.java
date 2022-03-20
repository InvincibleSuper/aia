package my.jwds.core.template;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import jdk.nashorn.internal.parser.JSONParser;
import my.jwds.core.AiaManager;
import my.jwds.core.api.InvokeApi;
import my.jwds.core.api.InvokeContentType;
import my.jwds.core.api.InvokeParam;
import my.jwds.core.model.*;
import my.jwds.utils.ModelUtils;
import my.jwds.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的模板生成器
 */
public class DefaultTemplateGenerator implements TemplateGenerator{

    /**
     * 生成模板
     *
     * @param aiaManager
     */
    @Override
    public void generate(AiaManager aiaManager) {
        aiaManager.allApi().forEach((group,apis)->{
            for (InvokeApi invokeApi : apis) {
                AiaTemplate template = new AiaTemplate();
                template.setUrl(invokeApi.getUrl());
                template.setName(resolveName(invokeApi));
                template.setGroup(group);
                generateParams(invokeApi.getParams(),template);
                generateHeader(invokeApi.getHeaders(), template);
                aiaManager.addTemplate(template);
            }
        });

    }

    protected void generateParams(List<InvokeParam> params ,AiaTemplate template){
        if (!params.isEmpty()){
            template.setParams(new LinkedHashMap<>());
        }
        for (InvokeParam param : params) {
            if (param.getContentType() == InvokeContentType.param){
                processParam(param,template);
            }else if (param.getContentType() == InvokeContentType.json){
                processJson(param,template);
            }else if (param.getContentType() == InvokeContentType.url){
                processUrl(param,template);
            }else if (param.getContentType() == InvokeContentType.file){
                processFile(param,template);
            }
        }
    }

    protected void processParam(InvokeParam param,AiaTemplate template){
        String prefix = param.getPrefix()==null? "": param.getPrefix()+".";
        ModelProperty modelProperty = param.getModel();
        if (modelProperty instanceof SimpleModelProperty){
            template.addParam(prefix+ param.getName(), ((SimpleModelProperty)modelProperty).getValue().toString(),param.getContentType().name());
        }else if (modelProperty instanceof ObjectModelProperty){
            ObjectModelProperty thisModel = (ObjectModelProperty) modelProperty;
            if (thisModel.getContainerContent() == null){
                for (ModelProperty property : thisModel.getModel().getProperties()) {
                    if (property instanceof SimpleModelProperty){
                        template.addParam(prefix+ property.getName(), ((SimpleModelProperty)property).getValue().toString(),param.getContentType().name());
                    }
                }
            }
        }else{
            ArrayModelProperty thisModel = (ArrayModelProperty) modelProperty;
            if (thisModel.getComponent() instanceof SimpleModelProperty){
                template.addParamArray(prefix+param.getName(), ((SimpleModelProperty)thisModel.getComponent()).getValue().toString(),param.getContentType().name());
            }
        }

    }
    protected void processJson(InvokeParam param,AiaTemplate template){
        Object model = ModelUtils.toMap(param.getModel());
        String value = JSONObject.toJSONString(model,true);
        template.addParam(param.getName(), value, param.getContentType().name() );
    }


    protected void processUrl(InvokeParam param,AiaTemplate template){
        template.addParam(param.getName(),param.getName(), param.getContentType().name());
    }
    protected void processFile(InvokeParam param,AiaTemplate template){
        template.addParam(param.getName(),null, param.getContentType().name());
    }

    protected void generateHeader(Map<String,String> headers,AiaTemplate template){
        Map<String,String> cloneHeaders = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            cloneHeaders.put(entry.getKey(),entry.getValue());
        }
        template.setHeaders(cloneHeaders);
    }



    protected String resolveName(InvokeApi invokeApi){
        String desc = invokeApi.getDefinition();
        if (!StringUtils.hasText(desc)){
            return invokeApi.getUrl().getUrl();
        }
        int brIndex = desc.indexOf("\n");
        if (brIndex == -1){
            return desc;
        }
        return desc.substring(0,brIndex);
    }
}
