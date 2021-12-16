package my.jwds.core;

public interface AiaApiScanner {

    /**
     * 扫描所有api
     * @param manager 一个门面类，封装功能实现的一个类
     */
    void startScanner();


    /**
     * 获取aia管理器
     * @return aia管理器
     */
    AiaManager getAiaManager();

    /**
     * 设置一个aia管理器
     * @param aiaManager
     */
    void setAiaManager(AiaManager aiaManager);
}
