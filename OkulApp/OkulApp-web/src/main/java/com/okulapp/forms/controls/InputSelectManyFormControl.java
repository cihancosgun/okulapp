/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms.controls;

import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author cihan
 */
public class InputSelectManyFormControl extends BaseFormControl<Object> {

    private List<SelectItem> items;

    public InputSelectManyFormControl() {
        this.setType("selectMany");        
    }

    public InputSelectManyFormControl(String name, String fieldName, String label) {
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setType("selectMany");
    }

    public List<SelectItem> getItems() {
        return items;
    }

    public void setItems(List<SelectItem> items) {
        this.items = items;
    }

}
