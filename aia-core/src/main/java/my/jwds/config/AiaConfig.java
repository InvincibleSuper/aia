package my.jwds.config;


import java.util.List;


public class AiaConfig {

    /**
     * 持久化
     */
    private boolean persist;

    /**
     * 源代码的路径，供注释解析
     */
    private String srcPath;

    /**
     * api解析允许的路径
     */
    private List<String> patternQualifiedNames;

    /**
     * api解析允许的url
     */
    private List<String> patternUrl;

    /**
     * 访问url，单服务不使用
     */
    private String accessUrl;


    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public boolean isPersist() {

        return persist;
    }

    public void setPersist(boolean persist) {
        this.persist = persist;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public List<String> getPatternQualifiedNames() {
        return patternQualifiedNames;
    }

    public void setPatternQualifiedNames(List<String> patternQualifiedNames) {
        this.patternQualifiedNames = patternQualifiedNames;
    }

    public List<String> getPatternUrl() {
        return patternUrl;
    }

    public void setPatternUrl(List<String> patternUrl) {
        this.patternUrl = patternUrl;
    }

    public AiaConfig(boolean persist, String srcPath) {
        this.persist = persist;
        this.srcPath = srcPath;
    }

    public AiaConfig() {
    }
}
