package my.jwds.springweb.parse;

import my.jwds.api.InvokeApi;
import my.jwds.api.InvokeUrl;
import my.jwds.api.definition.AiaDefinitionException;
import my.jwds.api.definition.resolver.DefinitionResolver;
import my.jwds.core.AiaManager;
import my.jwds.model.resolver.ModelResolver;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestMappingHandlerMappingParser extends AbstractHandlerMappingParser {



    private DefinitionResolver definitionResolver;


    private ModelResolver modelResolver;



    /**
     * 返回支持处理类
     * @return 支持的处理类
     */
    @Override
    public Class supportClass() {
        return  RequestMappingHandlerMapping.class;
    }

    /**
     * 解析 Spring的RequestMappingHandlerMapping
     * 获取RequestMappingInfo，并且对方法参数注解进行判断
     * @param aiaManager 总管理器
     * @param handlerMapping spring请求处理映射
     */
    @Override
    protected void onParse(AiaManager aiaManager,  HandlerMapping handlerMapping) {
        RequestMappingHandlerMapping r = (RequestMappingHandlerMapping) handlerMapping;
        Map<RequestMappingInfo, HandlerMethod> handlerMethods  = r.getHandlerMethods();
        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            List<InvokeUrl> invokeUrls = createInvokeUrl(requestMappingInfo);
        });
    }
    protected List<InvokeApi> createInvokeApi(RequestMappingInfo requestMappingInfo){
        List<InvokeUrl> invokeUrls = createInvokeUrl(requestMappingInfo);
        return null;
    }

    protected List<InvokeUrl> createInvokeUrl(RequestMappingInfo requestMappingInfo){
        List<InvokeUrl> res = new ArrayList<>();
        Set<String> paths = requestMappingInfo.getDirectPaths();
        for (String path : paths) {
            for (RequestMethod method : requestMappingInfo.getMethodsCondition().getMethods()) {
                InvokeUrl invokeUrl = new InvokeUrl();
                invokeUrl.setUrl(path);
                invokeUrl.setMethod(method.name());
            }
        }
        return res;
    }
}
