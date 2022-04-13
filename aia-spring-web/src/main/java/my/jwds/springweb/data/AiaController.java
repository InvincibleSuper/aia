package my.jwds.springweb.data;

import my.jwds.core.AiaManager;
import my.jwds.core.api.InvokeApi;
import my.jwds.core.api.InvokeUrl;
import my.jwds.core.template.AiaTemplate;
import my.jwds.core.template.TemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/aia/info")
public class AiaController {

    @Autowired
    private AiaManager aiaManager;

    @Autowired
    private TemplateGenerator templateGenerator;



    @GetMapping("api")
    @ResponseBody
    public Object allApi(HttpServletRequest request){
        return aiaManager.allApi();
    }


    @GetMapping("template")
    @ResponseBody
    public Object allTemplate(HttpServletRequest request){
        Map<String, List<AiaTemplate>> res = new LinkedHashMap<>();
        for (Map.Entry<String, Map<InvokeUrl, AiaTemplate>> entry : aiaManager.allTemplate().entrySet()) {
            res.put(entry.getKey(),new ArrayList(entry.getValue().values()));
        }
        return res;
    }


    @GetMapping("scan")
    @ResponseBody
    public boolean scan(){
        return aiaManager.isScan();
    }


    @GetMapping("templateGenerate")
    @ResponseBody
    public Object templateGenerate(String api){
        return templateGenerator.generate(aiaManager,api);
    }


}
