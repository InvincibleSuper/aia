package my.jwds.core.api.mgt;

import my.jwds.core.api.InvokeApi;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * api管理器
 */
public interface AiaApiManager {

    /**
     * 设置一个Api排序比较器
     * @param comparator
     */
    void setComparator(Comparator<InvokeApi> comparator);


    /**
     * 注册api
     * @param api api
     */
    void addApi(InvokeApi api);

    /**
     * 根据组注册api列表
     * @param group 组
     * @param apis api列表
     */
    void addGroupApi(String group,List<InvokeApi> apis);


    /**
     * 注册所有api
     * @param all 所有api
     */
    void addAll(List<InvokeApi> all);


    /**
     * 注册所有api
     * @param all 所有api
     */
    void addAll(Map<String,List<InvokeApi>> all);



    /**
     * 获取组
     * @return 组列表
     */
    Set<String> getApiGroup();

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
