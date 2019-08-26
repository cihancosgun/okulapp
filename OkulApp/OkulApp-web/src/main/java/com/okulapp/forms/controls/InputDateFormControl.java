/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms.controls;

import java.util.Date;

/**
 *
 * @author cihan
 */
public class InputDateFormControl extends BaseFormControl<Date> {

    private String pattern;

    public InputDateFormControl() {
        this.setType("date");
        this.setPattern("dd.MM.yyyy");
    }

    public InputDateFormControl(String name, String fieldName, String label) {
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setType("date");
        this.setPattern("dd.MM.yyyy");
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
