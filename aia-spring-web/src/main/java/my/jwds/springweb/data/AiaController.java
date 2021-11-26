package my.jwds.springweb.data;

import my.jwds.core.AiaManager;
import my.jwds.core.api.InvokeApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

@RequestMapping("/aia/info")
public class AiaController {


    private AiaManager aiaManager;

    public void setAiaManager(AiaManager aiaManager) {
        this.aiaManager = aiaManager;
    }



    @GetMapping("api")
    @ResponseBody
    public Map<String, List<InvokeApi>> allApi(){
        return aiaManager.allApi();
    }


}
