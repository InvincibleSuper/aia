package com.github.aia.springboot.samples.entity;

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

    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User() {
    }

    public User(String name, String pwd, User user) {
        this.name = name;
        this.pwd = pwd;
        this.user = user;
    }
}
