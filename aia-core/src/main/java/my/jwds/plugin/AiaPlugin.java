package my.jwds.plugin;

import java.util.List;
import java.util.Map;

public interface AiaPlugin {


    AiaPluginReturn invoke(Map<String,String> data);


    Map<String,String> dataModel();


    String getName();
}
