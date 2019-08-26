/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms.controls;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author cihan
 */
@FacesConverter("phoneConverter")
public class PhoneConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3");
    }

}
