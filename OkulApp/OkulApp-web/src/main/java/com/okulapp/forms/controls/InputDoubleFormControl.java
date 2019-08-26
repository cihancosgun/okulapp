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
public class InputDoubleFormControl extends BaseFormControl<Double> {

    private String decimalSeparator = ",";
    private String thousandSeparator = ".";
    private String decimalPlaces = "2";

    public InputDoubleFormControl() {
        this.setType("double");
    }

    public InputDoubleFormControl(String name, String fieldName, String label) {
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setType("double");
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public String getThousandSeparator() {
        return thousandSeparator;
    }

    public void setThousandSeparator(String thousandSeparator) {
        this.thousandSeparator = thousandSeparator;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public Double get() {
        return super.get(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValue() {
        return (Double) super.getValue(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof String && value.toString().isEmpty()) {
            value = null;
        }
        if (value != null) {
            super.setValue(Double.parseDouble(value.toString()));
        }
    }
}
