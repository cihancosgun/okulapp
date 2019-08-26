/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.navigation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.okulapp.data.AdvancedDataAdapter;

/**
 *
 * @author cihan
 */
public class Menu implements Serializable {

    private String link;
    private String iconClass;
    private String title;
    private List<Menu> menus;
    private String formCode;

    private Menu() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public static class Builder {

        Menu m;
        private static final String CRUD_FORM_CODE = "crudFormCode";
        private static final String TITLE_EN = "titleEn";
        private static final String EN_US = "en_US";
        private static final String TITLE = "title";
        private static final String TR_TR = "tr_TR";
        private static final String LINK = "link";
        private static final String ICON = "icon";
        private final String locale;

        public Builder(String locale) {
            m = new Menu();
            this.locale = locale;
        }

        public Builder createMenuFromDb(Map<String, Object> rec) {
            if (rec.get(ICON) != null) {
                m.setIconClass(rec.get(ICON).toString());
            }
            if (rec.get(LINK) != null) {
                m.setLink(rec.get(LINK).toString());
            }
            if (TR_TR.equals(locale)) {
                if (rec.get(TITLE) != null) {
                    m.setTitle(rec.get(TITLE).toString());
                }
            } else if (EN_US.equals(locale)) {
                if (rec.get(TITLE_EN) != null) {
                    m.setTitle(rec.get(TITLE_EN).toString());
                }
            } else {
                if (rec.get(TITLE) != null) {
                    m.setTitle(rec.get(TITLE).toString());
                }
            }

            if (rec.get(CRUD_FORM_CODE) != null) {
                m.setFormCode(rec.get(CRUD_FORM_CODE).toString());
            }
            return this;
        }

        public Menu build() {
            return m;
        }
    }

}
