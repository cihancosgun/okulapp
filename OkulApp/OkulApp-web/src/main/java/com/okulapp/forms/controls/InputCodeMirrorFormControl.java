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
public class InputCodeMirrorFormControl extends BaseFormControl<String> {

    private String mode;

    public InputCodeMirrorFormControl() {
        this.setType("codemirror");
    }

    public InputCodeMirrorFormControl(String name, String fieldName, String label) {
        this.setType("codemirror");
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
    }

    public InputCodeMirrorFormControl(String name, String fieldName, String label, Boolean required) {
        this.setType("codemirror");
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setRequired(required);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
