package my.jwds.core.security;

/**
 * 白名单，实现具体到某个接口的api生成，不符合白名单的接口跳过生成
 */
public interface WhiteList {


    /**
     * 新增白名单
     * @param whiteList 白名单
     */
    void addWhiteList(Object whiteList);


    /**
     * 是否包含
     * @param o 判断的数据
     * @return 结果
     */
    boolean include(Object o);

}
