/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.texts;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author cihan
 */
public class TextsMap extends HashMap {

    private static final String MESSAGE_PATH = "com.okulapp.texts";
    private static final Locale[] LOCALES = new Locale[]{Locale.forLanguageTag("tr-TR"), Locale.forLanguageTag("en-US")};
    private static TextsMap instance;

    private TextsMap() {
        for (Locale locale : LOCALES) {
            ResourceBundle messages = ResourceBundle.getBundle(MESSAGE_PATH, locale);
            this.put(locale.toString(), messages);
        }
    }

    public static TextsMap getInstance() {
        if (instance == null) {
            instance = new TextsMap();
        }
        return instance;
    }

    public String getText(String locale, String key) {
        if (instance == null) {
            return key;
        }
        ResourceBundle messages = (ResourceBundle) instance.get(locale);
        if (messages == null) {
            return key;
        }
        return messages.containsKey(key) ? messages.getString(key) : key;

    }

}
