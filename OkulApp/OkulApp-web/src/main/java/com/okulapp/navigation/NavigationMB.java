/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.navigation;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.inject.Inject;
import com.okulapp.dispatcher.DispatcherMB;
import com.okulapp.forms.CrudMB;
import com.okulapp.forms.Form;
import com.okulapp.texts.TextBundlerSMB;
import com.okulapp.security.SecurityMB;
import com.okulapp.data.okul.MyDataSBLocal;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author cihan
 */
@Named(value = "navigationMB")
@SessionScoped
public class NavigationMB extends Navigation {

    private @Inject
    DispatcherMB dispatcherMB;

    private @Inject
    CrudMB crudMB;

    private @Inject
    SecurityMB securityMB;

    private @Inject
    TextBundlerSMB textBundlerSMB;

    @EJB
    MyDataSBLocal myDataSB;

    @PostConstruct
    public void init() {
        this.setHeader("Menu");
        Navigation n = new Navigation.Builder(this.getHeader(),
                myDataSB.getAdvancedDataAdapter(),
                myDataSB.getDbName(),
                securityMB.getUserRolesArray(),
                textBundlerSMB.getLoceleStr())
                .createMenuFromDb()
                .build();
        this.setMenus(n.getMenus());
    }

    public void setFormFromMenu(Menu menu) throws CloneNotSupportedException {
        if (menu.getFormCode() != null) {
            crudMB.showCrudPage(menu.getFormCode());
        } else {
            dispatcherMB.setCurrentPage(new Form(menu.getTitle(), menu.getLink()));
        }

    }

}
