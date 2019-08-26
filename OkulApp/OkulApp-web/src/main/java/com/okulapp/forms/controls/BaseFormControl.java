/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms.controls;

/**
 *
 * @author cihan
 * @param <T>
 */
public class BaseFormControl<T> implements Cloneable {

    private String name;
    private String fieldName;

    private String label;
    private Boolean required;
    private Object value;
    private Object defaultValue = null;
    private Boolean showList = false;
    private T t;

    private String type;

    private String ajaxUpdateFieldName = "";
    private String ajaxUpdateFieldQueryFieldName = "";
    
    private String generatedId;

    public BaseFormControl() {

    }

    public BaseFormControl(String name, String fieldName, String label) {
        this.name = name;
        this.fieldName = fieldName;
        this.label = label;
    }

    public BaseFormControl(String name, String fieldName, String label, Boolean required, String type) {
        this.name = name;
        this.fieldName = fieldName;
        this.label = label;
        this.required = required;
        this.type = type;
    }

    public BaseFormControl(String name, String fieldName, String label, Boolean required, String type, Object defaultValue) {
        this.name = name;
        this.fieldName = fieldName;
        this.label = label;
        this.required = required;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public BaseFormControl(String name, String fieldName, String label, Boolean required, Object value, T t, String type, String ajaxUpdateFieldName) {
        this.name = name;
        this.fieldName = fieldName;
        this.label = label;
        this.required = required;
        this.value = value;
        this.t = t;
        this.type = type;
        this.ajaxUpdateFieldName = ajaxUpdateFieldName;
    }

    public String getAjaxUpdateFieldName() {
        return ajaxUpdateFieldName;
    }

    public String getAjaxUpdateFieldQueryFieldName() {
        return ajaxUpdateFieldQueryFieldName;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public String getName() {
        return name;
    }

    public void setAjaxUpdateFieldName(String ajaxUpdateFieldName) {
        this.ajaxUpdateFieldName = ajaxUpdateFieldName;
    }

    public void setAjaxUpdateFieldQueryFieldName(String ajaxUpdateFieldQueryFieldName) {
        this.ajaxUpdateFieldQueryFieldName = ajaxUpdateFieldQueryFieldName;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Object getValue() {
        return this.t;
    }

    public void setValue(Object value) {
        this.t = (T) value;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getShowList() {
        return showList;
    }

    public void setShowList(Boolean showList) {
        this.showList = showList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (BaseFormControl) super.clone();
    }

}
