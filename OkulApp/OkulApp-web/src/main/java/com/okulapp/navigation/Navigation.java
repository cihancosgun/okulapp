/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.data.AdvancedDataAdapter;

/**
 *
 * @author cihan
 */
public class Navigation implements Serializable {

    private String header;
    private List<Menu> menus;

    public Navigation() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public static class Builder {

        static final String menuTable = "menu";
        static final String subMenuTable = "subMenu";
        static final String ORDER = "order";
        static final String NAME = "name";
        static final String PARENT_MENU_NAME = "parentMenuName";

        private Navigation n;
        private final AdvancedDataAdapter ada;
        private final String dbName;
        private final Byte[] roles;
        private final String locale;

        public Builder(String header, AdvancedDataAdapter ada, String dbName, Byte[] roles, String locale) {
            n = new Navigation();
            n.setHeader(header);
            this.ada = ada;
            this.dbName = dbName;
            this.roles = roles;
            this.locale = locale;
        }

        public Builder withMenus(List<Menu> menus) {
            n.menus = menus;
            return this;
        }

        public Builder createMenuFromDb() {
            List<Menu> menus = new ArrayList();
            Map<String, Object> sort = new HashMap();
            sort.put(ORDER, 1);
            CrudListResult clr = ada.getSelectedDataAdapter().getSortedPagedList(dbName, menuTable, null, null, 0, 1000, sort);
            Map<String, Object> subMenuQuery = new HashMap();
            for (Map<String, Object> menu : clr) {
                subMenuQuery.put(PARENT_MENU_NAME, menu.get(NAME));
                CrudListResult clrSubMenus = ada.getSelectedDataAdapter().getSortedPagedList(dbName, subMenuTable, subMenuQuery, null, 0, 10000, sort);
                List<Menu> subMenu = null;
                if (clrSubMenus != null && clrSubMenus.getTotalRecordCount() > 0) {
                    subMenu = new ArrayList<>();
                    for (Map<String, Object> subMenuItem : clrSubMenus) {
                        subMenu.add(new Menu.Builder(locale).createMenuFromDb(subMenuItem).build());
                    }
                }
                Menu m = new Menu.Builder(locale).createMenuFromDb(menu).build();
                m.setMenus(subMenu);
                menus.add(m);
            }
            n.setMenus(menus);
            return this;
        }

        public Navigation build() {
            return n;
        }
    }

}
