package my.jwds.config;


public class AiaConfig {

    private  boolean persist = true;


    private String srcPath;

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
}
