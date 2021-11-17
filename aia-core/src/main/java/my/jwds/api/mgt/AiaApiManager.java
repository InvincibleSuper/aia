package my.jwds.api.mgt;

import my.jwds.api.InvokeApi;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AiaApiManager {


    /**
     * 注册api
     * @param api api
     */
    void registerApi(InvokeApi api);

    /**
     * 根据组注册api列表
     * @param group 组
     * @param apis api列表
     */
    void registerGroupApi(String group,List<InvokeApi> apis);

    /**
     * 注册所有api
     * @param all 所有api
     */
    void registerAll(Map<String,List<InvokeApi>> all);

    /**
     * 获取组
     * @return 组列表
     */
    Set<String> getGroup();

    /**
     * 获取组
     * @param group 组名
     * @return
     */
    List<InvokeApi> getGroupInvokeApi(String group);


    /**
     * 全部api
     * @return 组和api的键值对
     */
    Map<String,List<InvokeApi>> allApi();

}
