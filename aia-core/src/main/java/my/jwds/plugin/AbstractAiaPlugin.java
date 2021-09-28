package my.jwds.plugin;

import java.util.List;
import java.util.Map;

public abstract class AbstractAiaPlugin implements AiaPlugin{

    private String name;


    private Map<String,String> dataModel;

    public AbstractAiaPlugin(String name, Map<String, String> dataModel) {
        this.name = name;
        this.dataModel = dataModel;
    }

    @Override
    public Map<String, String> dataModel() {
        return dataModel;
    }

    @Override
    public String getName() {
        return name;
    }
}
