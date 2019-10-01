/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.dispatcher;

import com.okulapp.chat.ChatMB;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.forms.CrudMB;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import com.okulapp.forms.Form;
import com.okulapp.notify.BoardMB;
import com.okulapp.notify.BoardUtil;
import com.okulapp.security.SecurityMB;
import com.okulapp.texts.TextBundlerSMB;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.bson.types.ObjectId;

/**
 *
 * @author cihan
 */
@Named(value = "dispatcherMB")
@SessionScoped
public class DispatcherMB implements Serializable {

    private @Inject
    TextBundlerSMB textBundlerSMB;

    @Inject
    CrudMB crudMB;

    @Inject
    ChatMB chatMB;

    @Inject
    BoardMB boardMB;

    @Inject
    SecurityMB securityMB;

    @EJB
    MyDataSBLocal myDataSB;

    private Form currentPage;

    public DispatcherMB() {
    }

    public Form getCurrentPage() {
        if (currentPage == null) {
            setCurrentPage(new Form(textBundlerSMB.getText("home"), "/pages/home.xhtml"));
        }
        Logger.getLogger("Dispatcher").log(Level.SEVERE, "getCurrentPage", currentPage.getPageUrl());
        return currentPage;
    }

    public void setCurrentPage(Form currentPage) {
        this.currentPage = currentPage;
    }

    public void switchToChatPage(Map<String, Object> chat) {
        chatMB.refreshChat();
        chatMB.refreshMyUnreadMessages();
        if (chat != null) {
            chatMB.switchToChat(chat);
        } else {
            chatMB.showRecentsForm();
        }
        setCurrentPage(new Form("Mesajlaşma", "/pages/chat.xhtml"));
    }

    public void switchToBoardPage() {
        boardMB.refreshMyBoard();
        BoardUtil.setReaded(myDataSB, boardMB.getMyUnreadedBoard(), securityMB.getRemoteUserName());
        setCurrentPage(new Form("Pano", "/pages/home.xhtml"));
    }

    public void switchToShowImagePage(ObjectId image, Map<String, Object> notify) {
        boardMB.setCurrentImage(image);
        boardMB.setCurrentNotify(notify);
        setCurrentPage(new Form("Resim", "/pages/showImage.xhtml"));
    }

    public void switchToShowNotify(Map<String, Object> notify) {
        boardMB.setCurrentNotify(notify);
        setCurrentPage(new Form("Pano", "/pages/showNotify.xhtml"));
    }
}
