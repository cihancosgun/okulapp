/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.dispatcher;

import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import com.okulapp.forms.Form;
import com.okulapp.texts.TextBundlerSMB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author cihan
 */
@Named(value = "dispatcherMB")
@SessionScoped
public class DispatcherMB implements Serializable {

    private @Inject
    TextBundlerSMB textBundlerSMB;

    private Form currentPage;

    public DispatcherMB() {
    }

    public Form getCurrentPage() {
        if (currentPage == null) {

            currentPage = new Form(textBundlerSMB.getText("home"), "/pages/home.xhtml");
        }
        return currentPage;
    }

    public void setCurrentPage(Form currentPage) {
        this.currentPage = currentPage;
    }

    public void switchToChatPage() {
        currentPage = new Form("Mesajlaşma", "/pages/chat.xhtml");
    }
    
    public void switchToBoardPage() {
        currentPage = new Form("Pano", "/pages/home.xhtml");
    }
}
