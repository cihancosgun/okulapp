/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.security;

/**
 *
 * @author cihan
 */
public class CrudSecurityRole {

    private String role;
    private Boolean canAddNew;
    private Boolean canEdit;
    private Boolean canDelete;
    private Boolean canList;

    public CrudSecurityRole() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getCanAddNew() {
        return canAddNew;
    }

    public void setCanAddNew(Boolean canAddNew) {
        this.canAddNew = canAddNew;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Boolean getCanList() {
        return canList;
    }

    public void setCanList(Boolean canList) {
        this.canList = canList;
    }

    @Override
    public String toString() {
        return role.concat("_").concat(canAddNew ? "1" : "0").concat(canEdit ? "1" : "0").concat(canDelete ? "1" : "0").concat(canList ? "1" : "0");
    }

    public static CrudSecurityRole parseFromString(String parseValue) {
        CrudSecurityRole result = new CrudSecurityRole();
        String[] parts = parseValue.split("_");
        result.role = parts[0];
        result.canAddNew = parts[1].substring(0, 1).equals("1");
        result.canEdit = parts[1].substring(1, 1).equals("1");
        result.canDelete = parts[1].substring(2, 1).equals("1");
        result.canList = parts[1].substring(3, 1).equals("1");
        return result;
    }
}
