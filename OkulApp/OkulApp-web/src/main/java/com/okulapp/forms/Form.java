/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms;

import java.util.List;

/**
 *
 * @author cihan
 */
public class Form implements Cloneable {

    private String title;
    private String pageUrl;
    private List<String> accessRoles;

    public Form() {
    }

    public Form(String title, String pageUrl) {
        this.title = title;
        this.pageUrl = pageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public List<String> getAccessRoles() {
        return accessRoles;
    }

    public void setAccessRoles(List<String> accessRoles) {
        this.accessRoles = accessRoles;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
