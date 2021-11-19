package my.jwds.springweb;

import my.jwds.core.AiaApiScanner;
import my.jwds.core.AiaManager;
import my.jwds.springweb.parse.SpringHandlerMappingParserRegister;
import my.jwds.springweb.utils.HandlerMappingUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

public class SpringWebAiaScanner implements AiaApiScanner {

    private ApplicationContext ac;

    private SpringHandlerMappingParserRegister parserRegister;

    public void setAc(ApplicationContext ac) {
        this.ac = ac;
    }

    public void setParserRegister(SpringHandlerMappingParserRegister parserRegister) {
        this.parserRegister = parserRegister;
    }

    @PostConstruct
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
