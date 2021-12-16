package my.jwds.core.template;

import my.jwds.core.AiaManager;
import my.jwds.core.api.InvokeApi;
import my.jwds.core.api.InvokeContentType;
import my.jwds.core.api.InvokeParam;
import my.jwds.core.model.*;

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
                template.setName(invokeApi.getUrl().getUrl());
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
            if (param.getContentType().equals(InvokeContentType.param.name())){
                processParam(param,template);
            }else if (param.getContentType().equals(InvokeContentType.json.name())){
                processJson(param,template);
            }else if (param.getContentType().equals(InvokeContentType.url.name())){
                processUrl(param,template);
            }else if (param.getContentType().equals(InvokeContentType.file.name())){
                processFile(param,template);
            }
        }
    }

    protected void processParam(InvokeParam param,AiaTemplate template){
        String prefix = param.getPrefix()==null? "": param.getPrefix()+".";
        ModelProperty modelProperty = param.getModel();
        if (modelProperty instanceof SimpleModelProperty){
            template.addParam(prefix+ param.getName(), ((SimpleModelProperty)modelProperty).getValue().toString(),param.getContentType());
        }else if (modelProperty instanceof ObjectModelProperty){
            ObjectModelProperty thisModel = (ObjectModelProperty) modelProperty;
            if (thisModel.getContainerContent() == null){
                for (ModelProperty property : thisModel.getModel().getProperties()) {
                    if (property instanceof SimpleModelProperty){
                        template.addParam(prefix+ param.getName(), ((SimpleModelProperty)property).getValue().toString(),param.getContentType());
                    }
                }
            }
        }else{
            ArrayModelProperty thisModel = (ArrayModelProperty) modelProperty;
            if (thisModel.getComponent() instanceof SimpleModelProperty){
                template.addParam(prefix+param.getName(), ((SimpleModelProperty)thisModel.getComponent()).getValue().toString(),param.getContentType());
            }
        }

    }
    protected void processJson(InvokeParam param,AiaTemplate template){

    }
    protected void processUrl(InvokeParam param,AiaTemplate template){
        template.addParam(param.getName(),param.getName(), param.getContentType());
    }
    protected void processFile(InvokeParam param,AiaTemplate template){
        template.addParam(param.getName(),null, param.getContentType());
    }

    protected void generateHeader(Map<String,String> headers,AiaTemplate template){
        Map<String,String> cloneHeaders = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            cloneHeaders.put(entry.getKey(),entry.getValue());
        }
        template.setHeaders(cloneHeaders);
    }
}
