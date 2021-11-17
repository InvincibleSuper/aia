package my.jwds.core;

public interface AiaApiScanner {

    /**
     * 扫描所有api
     * @param manager 一个门面类，封装功能实现的一个类
     */
    void startScanner(AiaManager manager);

}
