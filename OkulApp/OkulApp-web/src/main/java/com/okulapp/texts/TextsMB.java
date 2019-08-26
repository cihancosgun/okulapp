/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.texts;

import java.util.HashMap;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author cihan
 */
@ManagedBean(name = "texts", eager = true)
@SessionScoped
public class TextsMB extends HashMap<String, String> {

    public static final String SELECTED_LANG = "SELECTED_LANG";

    public TextsMB() {
    }

    @Override
    public String get(Object key) {
        return getMessage((String) key);
    }

    public static String getMessage(String key) {
        if (key == null) {
            return null;
        }
        try {
            Locale locale = null; //FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (locale == null) {
                locale = Locale.forLanguageTag("tr-TR");
            }
            Locale selected_locale = (Locale) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(SELECTED_LANG);
            if (selected_locale != null) {
                locale = selected_locale;
            }

            return TextsMap.getInstance().getText(locale.toString(), key);

        } catch (Exception e) {
            return key;
        }
    }
}
