package my.jwds.springboot.samples.entity;

/**
 * 用户
 */
public class User {

    /**
     * 名称
     */
    private String name;


    /**
     * 密码
     */
    private String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
