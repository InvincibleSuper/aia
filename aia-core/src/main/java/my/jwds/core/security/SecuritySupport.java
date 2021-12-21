package my.jwds.core.security;


public class SecuritySupport {

    private QualifiedNameWhiteList qualifiedNameWhiteList;

    private UrlWhiteList urlWhiteList;


    public QualifiedNameWhiteList getQualifiedNameWhiteList() {
        return qualifiedNameWhiteList;
    }

    public void setQualifiedNameWhiteList(QualifiedNameWhiteList qualifiedNameWhiteList) {
        this.qualifiedNameWhiteList = qualifiedNameWhiteList;
    }

    public UrlWhiteList getUrlWhiteList() {
        return urlWhiteList;
    }

    public void setUrlWhiteList(UrlWhiteList urlWhiteList) {
        this.urlWhiteList = urlWhiteList;
    }


    public boolean qualifiedNameInclude(String qualifiedName){
        if (getQualifiedNameWhiteList() == null)return true;
        return getQualifiedNameWhiteList().include(qualifiedName);
    }

    public boolean urlInclude(String url){
        if (getUrlWhiteList() == null)return true;
        return getUrlWhiteList().include(url);
    }
}
