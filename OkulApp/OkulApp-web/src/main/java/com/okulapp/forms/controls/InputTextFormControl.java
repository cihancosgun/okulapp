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
public class InputTextFormControl extends BaseFormControl<String> {

    public InputTextFormControl() {
        this.setType("text");
    }

    public InputTextFormControl(String name, String fieldName, String label) {
        this.setType("text");
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
    }

    public InputTextFormControl(String name, String fieldName, String label, Boolean required) {
        this.setType("text");
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setRequired(required);
    }
}
