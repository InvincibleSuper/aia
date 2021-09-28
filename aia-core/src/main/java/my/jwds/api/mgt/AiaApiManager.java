package my.jwds.api.mgt;

import my.jwds.api.InvokeApi;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AiaApiManager {


    /**
     * 注册api
     * @param api
     */
    void registerApi(InvokeApi api);

    /**
     * 注册api
     */
    void registerGroupApi(String group,List<InvokeApi> apis);

    /**
     * 注册api
     */
    void registerAll(Map<String,List<InvokeApi>> all);

    /**
     * 获取组
     * @return
     */
    Set<String> getGroup();

    /**
     * 获取组
     * @param group
     * @return
     */
    List<InvokeApi> getGroupInvokeApi(String group);


    /**
     * 全部api
     * @return
     */
    Map<String,List<InvokeApi>> allApi();

}
