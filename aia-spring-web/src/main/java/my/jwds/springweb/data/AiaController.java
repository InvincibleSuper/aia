package my.jwds.springweb.data;

import my.jwds.core.AiaContext;
import my.jwds.core.api.InvokeUrl;
import my.jwds.core.template.AiaTemplate;
import my.jwds.core.template.TemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/aia/info")
public class AiaController {

    @Autowired
    private AiaContext aiaContext;

    @Autowired
    private TemplateGenerator templateGenerator;



    @GetMapping("api")
    @ResponseBody
    public Object allApi(HttpServletRequest request){
        return aiaContext.getApiManager().allApi();
    }


    @GetMapping("template")
    @ResponseBody
    public Object allTemplate(HttpServletRequest request){
        Map<String, List<AiaTemplate>> res = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, AiaTemplate>> entry : aiaContext.getTemplateManager().allTemplate().entrySet()) {
            res.put(entry.getKey(),new ArrayList(entry.getValue().values()));
        }
        return res;
    }


    @GetMapping("complete")
    @ResponseBody
    public boolean complete(){
        return aiaContext.isGenerateTemplate();
    }


    @GetMapping("templateGenerate")
    @ResponseBody
    public Object templateGenerate(String api){
        return templateGenerator.generate(aiaContext,api);
    }




    @PostMapping("updateTemplate")
    @ResponseBody
    public Object updateTemplate(@RequestBody AiaTemplate template){
        aiaContext.getTemplateManager().updateTemplate(template);
        return true;
    }
}
