package my.jwds.springweb;

import my.jwds.core.AiaApiScanner;
import my.jwds.core.AiaManager;
import my.jwds.springweb.parse.SpringHandlerMappingParserComposite;
import my.jwds.springweb.utils.HandlerMappingUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.PostConstruct;
import java.util.*;

public class SpringWebAiaScanner implements AiaApiScanner {

    private ApplicationContext ac;

    private SpringHandlerMappingParserComposite parserRegister;

    private AiaManager aiaManager;

    public SpringWebAiaScanner(ApplicationContext ac, SpringHandlerMappingParserComposite parserRegister,AiaManager aiaManager) {
        this.ac = ac;
        this.parserRegister = parserRegister;
        this.setAiaManager(aiaManager);
    }

    @PostConstruct
    @Override
    public void startScanner() {
        new Thread(()->{
            List<HandlerMapping> handlerMappings = HandlerMappingUtils.get(ac);
            for (HandlerMapping handlerMapping : handlerMappings) {
                parserRegister.parse(aiaManager,handlerMapping);
            }
        }).start();
    }

    /**
     * 获取aia管理器
     *
     * @return aia管理器
     */
    @Override
    public AiaManager getAiaManager() {
        return aiaManager;
    }

    /**
     * 设置一个aia管理器
     *
     * @param aiaManager
     */
    @Override
    public void setAiaManager(AiaManager aiaManager) {
        this.aiaManager = aiaManager;
    }
}
