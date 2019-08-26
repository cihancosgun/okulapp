/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.texts;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.context.FacesContext;
import static com.okulapp.texts.TextsMB.SELECTED_LANG;

/**
 *
 * @author cihan
 */
@Named(value = "textBundlerSMB")
@SessionScoped
public class TextBundlerSMB implements Serializable {

    public TextBundlerSMB() {
    }

    public String getLoceleStr() {
        Locale locale = null;//FacesContext.getCurrentInstance().getViewRoot().getLocale();
        if (locale == null) {
            locale = Locale.forLanguageTag("tr-TR");
        }
        Locale selected_locale = (Locale) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(SELECTED_LANG);
        if (selected_locale != null) {
            locale = selected_locale;
        }
        return locale.toString();
    }

    public String getText(String key) {
        return TextsMap.getInstance().getText(getLoceleStr(), key);
    }
}
