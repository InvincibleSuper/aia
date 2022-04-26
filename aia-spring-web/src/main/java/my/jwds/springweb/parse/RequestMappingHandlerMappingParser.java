package my.jwds.springweb.parse;

import com.alibaba.fastjson.JSONObject;
import my.jwds.core.api.*;
import my.jwds.core.api.definition.MethodDefinition;
import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.AiaManager;
import my.jwds.core.model.ModelProperty;
import my.jwds.core.model.resolver.ModelResolver;
import my.jwds.core.security.SecuritySupport;
import my.jwds.springweb.parse.method.HandlerMethodArgumentResolver;
import my.jwds.springweb.parse.method.HandlerMethodArgumentResolverRegister;
import my.jwds.springweb.utils.ContentTypeTable;
import my.jwds.utils.MethodUtils;
import my.jwds.utils.ModelUtils;
import my.jwds.utils.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


import java.util.*;


/**
 * 对{@link RequestMappingHandlerMapping}的解析支持
 */
public class RequestMappingHandlerMappingParser extends AbstractHandlerMappingParser {



    private DefinitionResolver definitionResolver;

    private HandlerMethodArgumentResolverRegister argumentResolverRegister;

    private ModelResolver modelResolver;

    private SecuritySupport securitySupport;

    public RequestMappingHandlerMappingParser(DefinitionResolver definitionResolver
            , ModelResolver modelResolver
            , HandlerMethodArgumentResolverRegister argumentResolverRegister
            , SecuritySupport securitySupport) {
        this.definitionResolver = definitionResolver;
        this.argumentResolverRegister = argumentResolverRegister;
        this.modelResolver = modelResolver;
        this.securitySupport = securitySupport;
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
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = r.getHandlerMethods();
        List<InvokeApi> result = new ArrayList<>();
        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            result.addAll(createInvokeApi(requestMappingInfo,handlerMethod));
        });
        aiaManager.addAll(result);
    }



    protected List<InvokeApi> createInvokeApi(RequestMappingInfo requestMappingInfo,HandlerMethod handlerMethod){
        System.out.println(resolveGroup(handlerMethod));
        if (!securitySupport.qualifiedNameInclude(MethodUtils.getQualifiedName(handlerMethod.getMethod()))){
            return new ArrayList<>();
        }
        List<InvokeUrl> invokeUrls = createInvokeUrl(requestMappingInfo);
        InvokerReturnValue returnValue = resolverReturn(handlerMethod);
        ArrayList<InvokeParam> invokeParams = createInvokeParam(handlerMethod);
        LinkedHashMap<String,String> headers = resolveHeader(requestMappingInfo);
        ensureContentType(headers,invokeParams,requestMappingInfo);
        List<InvokeApi> result = new ArrayList<>();
        for (InvokeUrl invokeUrl : invokeUrls) {
            if (securitySupport.urlInclude(invokeUrl.getUrl())){
                InvokeApi invokeApi = new InvokeApi();
                invokeApi.setUrl(invokeUrl);
                invokeApi.setParams((List<InvokeParam>) invokeParams.clone());
                invokeApi.setGroup(resolveGroup(handlerMethod));
                invokeApi.setDefinition(definitionResolver.resolveMethodDefinition(handlerMethod.getMethod()));
                invokeApi.setHeaders((Map<String, String>) headers.clone());
                invokeApi.setReturnValue(returnValue.clone());
                result.add(invokeApi);
            }
        }
        return result;
    }


    protected void ensureContentType(LinkedHashMap<String,String> headers,ArrayList<InvokeParam> invokeParams,RequestMappingInfo requestMappingInfo){
        InvokeContentType max = InvokeContentType.url;
        for (InvokeParam param : invokeParams) {
            if (param.getContentType().getOrder() >max.getOrder()){
                max = param.getContentType();
            }
        }
        Set<MediaType> producesMediaType = requestMappingInfo.getProducesCondition().getProducibleMediaTypes();
        MediaType contentType = null;
        MediaType[] optionalMediaType = ContentTypeTable.find(max);

        if ((producesMediaType == null || producesMediaType.isEmpty()) && optionalMediaType != null ){
            contentType = optionalMediaType[0];
        }else if (optionalMediaType == null){
            contentType = MediaType.APPLICATION_FORM_URLENCODED;
        }else{
            for (MediaType mediaType : optionalMediaType) {
                if (producesMediaType.contains(mediaType)){
                    contentType = mediaType;
                    break;
                }
            }
        }
        if (contentType == null){
            headers.put("Content-Type","no-support");
        }else{
            headers.put("Content-Type",contentType.toString());
        }

    }

    protected ArrayList<InvokeUrl> createInvokeUrl(RequestMappingInfo requestMappingInfo){
        ArrayList<InvokeUrl> res = new ArrayList<>();
        Set<String> paths = requestMappingInfo.getPatternValues();
        for (String path : paths) {
            for (RequestMethod method : requestMappingInfo.getMethodsCondition().getMethods()) {
                InvokeUrl invokeUrl = new InvokeUrl();
                invokeUrl.setUrl(path);
                invokeUrl.setMethod(method.name());
                res.add(invokeUrl);
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

        return headers;
    }

    protected InvokerReturnValue resolverReturn(HandlerMethod handlerMethod){
        MethodParameter returnParam = handlerMethod.getReturnType();
        ModelProperty returnModel = modelResolver.resolve(returnParam.getGenericParameterType());
        boolean dataOrPage = (AnnotatedElementUtils.hasAnnotation(returnParam.getContainingClass(), ResponseBody.class) ||
                returnParam.hasMethodAnnotation(ResponseBody.class));
        String example = JSONObject.toJSONString(ModelUtils.toMap(returnModel),true);
        MethodDefinition methodDefinition = definitionResolver.resolveMethod(handlerMethod.getMethod().getDeclaringClass(),handlerMethod.getMethod());
        String definition = methodDefinition==null?null:methodDefinition.getReturnDefinition();
        return new InvokerReturnValue(definition,returnModel,example,dataOrPage);
    }

    /**
     * 获取组，默认获取定义类注释第一行，没有注释使用简单类名称
     * @param handlerMethod
     * @return
     */
    protected String resolveGroup(HandlerMethod handlerMethod){
        Class clz = handlerMethod.getMethod().getDeclaringClass();
        String desc = definitionResolver.resolveClassDefinition(clz);
        if (!StringUtils.hasText(desc)){
            return clz.getSimpleName();
        }
        int brIndex = desc.indexOf("\n");
        if (brIndex == -1){
            return desc;
        }
        return desc.substring(0,brIndex);
    }
}
