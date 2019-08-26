/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms.controls;

/**
 *
 * @author cihan
 */
public class InputCheckBoxControl extends BaseFormControl<Boolean> {

    public InputCheckBoxControl() {
        this.setType("checkbox");
    }

    public InputCheckBoxControl(String name, String fieldName, String label) {
        this.setType("checkbox");
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
    }

    public InputCheckBoxControl(String name, String fieldName, String label, Boolean required) {
        this.setType("checkbox");
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setRequired(required);
    }
}
