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
public class InputIntegerFormControl extends BaseFormControl<Integer> {

    public InputIntegerFormControl() {
        this.setType("integer");
    }

    public InputIntegerFormControl(String name, String fieldName, String label) {
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setType("integer");
    }

    @Override
    public Integer get() {
        return super.get(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValue() {
        return (Integer) super.getValue(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof String && value.toString().isEmpty()) {
            value = null;
        }
        if (value != null) {
            if (value instanceof Double) {
                value = value.toString().replace(".0", "");
            }
            super.setValue(Integer.parseInt(value.toString()));
        }
    }

}
