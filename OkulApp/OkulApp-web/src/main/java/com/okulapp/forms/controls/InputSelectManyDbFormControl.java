/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms.controls;

import com.okulapp.data.AdvancedDataAdapter;
import java.util.ArrayList;

/**
 *
 * @author cihan
 */
public class InputSelectManyDbFormControl extends InputSelectOneFormControl {

    private AdvancedDataAdapter.DbType dbType;
    private String dbName;
    private String tableName;
    private String itemQuery;
    private String itemValueField;
    private String itemLabelField;

    public InputSelectManyDbFormControl() {
        this.setType("selectManyDb");
        this.set(new ArrayList());
        this.setValue(new ArrayList());
    }

    public InputSelectManyDbFormControl(String name, String fieldName, String label) {
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setType("selectManyDb");
    }

    public AdvancedDataAdapter.DbType getDbType() {
        return dbType;
    }

    public void setDbType(AdvancedDataAdapter.DbType dbType) {
        this.dbType = dbType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getItemQuery() {
        return itemQuery;
    }

    public void setItemQuery(String itemQuery) {
        this.itemQuery = itemQuery;
    }

    public String getItemValueField() {
        return itemValueField;
    }

    public void setItemValueField(String itemValueField) {
        this.itemValueField = itemValueField;
    }

    public String getItemLabelField() {
        return itemLabelField;
    }

    public void setItemLabelField(String itemLabelField) {
        this.itemLabelField = itemLabelField;
    }

}
