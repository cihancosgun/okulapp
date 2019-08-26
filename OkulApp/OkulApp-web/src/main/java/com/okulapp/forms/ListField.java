/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms;

import com.okulapp.forms.controls.BaseFormControl;

/**
 *
 * @author cihan
 */
public class ListField {

    private String fieldName;
    private String label;
    private BaseFormControl formControl;

    public ListField() {
    }

    public ListField(String fieldName, String label, BaseFormControl formControl) {
        this.fieldName = fieldName;
        this.label = label;
        this.formControl = formControl;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BaseFormControl getFormControl() {
        return formControl;
    }

    public void setFormControl(BaseFormControl formControl) {
        this.formControl = formControl;
    }

}
