/*
 * 
 * 
 * 
 */
package com.okulapp.api;

/**
 *
 * @author Doruk Fişek
 */
public class MyLoginBean {

    private String loginName;
    private String token;

    public MyLoginBean() {
    }

    public MyLoginBean(String loginName) {
        this.loginName = loginName;
    }

    public MyLoginBean(String loginName, String token) {
        this.loginName = loginName;
        this.token = token;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getToken() {
        return token;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
