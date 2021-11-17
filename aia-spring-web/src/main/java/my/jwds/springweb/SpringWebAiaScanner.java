package my.jwds.springweb;

import my.jwds.core.AiaApiScanner;
import my.jwds.core.AiaManager;
import my.jwds.definition.resolver.DefinitionResolver;
import my.jwds.model.ModelPropertyResolveInfo;
import my.jwds.model.resolver.ModelResolver;
import my.jwds.springweb.parse.SpringHandlerMappingParserRegister;
import my.jwds.springweb.parse.SpringHandlerMappingParserRegisterSupport;
import my.jwds.springweb.utils.HandlerMappingUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerMapping;

import java.util.*;

public class SpringWebAiaScanner implements AiaApiScanner {


    private ApplicationContext ac;


    private DefinitionResolver definitionResolver;

    private ModelResolver<ModelPropertyResolveInfo> modelResolver;

    private SpringHandlerMappingParserRegister parserRegister;



    public SpringWebAiaScanner(ApplicationContext ac, DefinitionResolver definitionResolver, ModelResolver modelResolver) {
        this.ac = ac;
        this.definitionResolver = definitionResolver;
        this.modelResolver = modelResolver;
        parserRegister = new SpringHandlerMappingParserRegisterSupport();
    }

    @Override
    public void startScanner(AiaManager manager) {
        new Thread(()->{
            List<HandlerMapping> handlerMappings = HandlerMappingUtils.get(ac);
            for (HandlerMapping handlerMapping : handlerMappings) {
                parserRegister.findParser(handlerMapping).parse(manager,handlerMapping);
            }
        }).start();
    }



}
