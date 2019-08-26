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
public class InputMaskFormControl extends BaseFormControl<String> {

    private String mask;

    public InputMaskFormControl() {
        this.setType("mask");
    }

    public InputMaskFormControl(String name, String fieldName, String label) {
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setType("mask");
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

}
