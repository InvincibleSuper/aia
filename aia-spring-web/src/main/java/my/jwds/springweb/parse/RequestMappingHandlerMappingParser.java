package my.jwds.springweb.parse;

import my.jwds.api.InvokeApi;
import my.jwds.api.InvokeParam;
import my.jwds.api.InvokeUrl;
import my.jwds.api.definition.resolver.DefinitionResolver;
import my.jwds.core.AiaManager;
import my.jwds.springweb.parse.method.HandlerMethodArgumentResolver;
import my.jwds.springweb.parse.method.HandlerMethodArgumentResolverRegister;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

public class RequestMappingHandlerMappingParser extends AbstractHandlerMappingParser {



    private DefinitionResolver definitionResolver;


    private HandlerMethodArgumentResolverRegister argumentResolverRegister;


    public RequestMappingHandlerMappingParser(DefinitionResolver definitionResolver, HandlerMethodArgumentResolverRegister argumentResolverRegister) {
        this.definitionResolver = definitionResolver;
        this.argumentResolverRegister = argumentResolverRegister;
    }

    /**
     * 返回支持处理类
     * @return 支持的处理类
     */
    @Override
    public Class supportClass() {
        return RequestMappingHandlerMapping.class;
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
        List<InvokeApi> result = new ArrayList<>();
        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            result.addAll(createInvokeApi(requestMappingInfo,handlerMethod));
        });
        aiaManager.addAll(result);
    }



    protected List<InvokeApi> createInvokeApi(RequestMappingInfo requestMappingInfo,HandlerMethod handlerMethod){
        List<InvokeUrl> invokeUrls = createInvokeUrl(requestMappingInfo);
        ArrayList<InvokeParam> invokeParams = createInvokeParam(handlerMethod);
        LinkedHashMap<String,String> headers = resolveHeader(requestMappingInfo);
        List<InvokeApi> result = new ArrayList<>();
        for (InvokeUrl invokeUrl : invokeUrls) {
            InvokeApi invokeApi = new InvokeApi();
            invokeApi.setUrl(invokeUrl);
            invokeApi.setParams((List<InvokeParam>) invokeParams.clone());
            invokeApi.setGroup("Default");
            invokeApi.setDefinition(definitionResolver.resolveMethodDefinition(handlerMethod.getMethod()));
            invokeApi.setHeaders((Map<String, String>) headers.clone());
            result.add(invokeApi);
        }
        return result;
    }

    protected ArrayList<InvokeUrl> createInvokeUrl(RequestMappingInfo requestMappingInfo){
        ArrayList<InvokeUrl> res = new ArrayList<>();
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


    protected ArrayList<InvokeParam> createInvokeParam(HandlerMethod handlerMethod){
        ArrayList<InvokeParam> result = new ArrayList<>();
        for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
            HandlerMethodArgumentResolver argumentResolver = argumentResolverRegister.findArgumentResolver(methodParameter);
            InvokeParam invokeParam = null;
            if (argumentResolver != null){
                invokeParam = argumentResolver.resolve(methodParameter);
            }
            result.add(invokeParam);
        }
        return result;

    }


    protected LinkedHashMap<String,String> resolveHeader(RequestMappingInfo requestMappingInfo){
        LinkedHashMap<String,String> headers = new LinkedHashMap<>();
        for (NameValueExpression<String> expression : requestMappingInfo.getHeadersCondition().getExpressions()) {
            headers.put(expression.getName(),expression.getValue());
        }
        StringBuilder producerMediaType = new StringBuilder();
        for (MediaType mediaType : requestMappingInfo.getProducesCondition().getProducibleMediaTypes()) {
            producerMediaType.append(mediaType.getType());
            producerMediaType.append(",");
        }
        if (producerMediaType.length() != 0){
            headers.put("Content-Type",producerMediaType.substring(0,producerMediaType.length()-1));
        }
        return headers;
    }
}
