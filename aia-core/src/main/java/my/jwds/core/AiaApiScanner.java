package my.jwds.core;

public interface AiaApiScanner {

    /**
     * 扫描所有api
     */
    void startScanner();


    /**
     * 获取aia管理器
     * @return aia管理器
     */
    AiaContext getAiaContext();

    /**
     * 设置一个aia管理器
     * @param aiaContext
     */
    void setAiaContext(AiaContext aiaContext);
}
