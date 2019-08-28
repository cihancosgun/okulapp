/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms.controls;

import org.bson.types.ObjectId;

/**
 *
 * @author cihan
 */
public class InputImage extends BaseFormControl<ObjectId> {

    public InputImage() {
        this.setType("image");
    }

    public InputImage(String name, String fieldName, String label) {
        this.setName(name);
        this.setFieldName(fieldName);
        this.setLabel(label);
        this.setType("image");
    }
}
