package com.imooc.vo;

/**
 * @Author Linton
 * @Date 2019/7/31 20:22
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */

public class User {
    private  String username;
    private String password;
    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}

