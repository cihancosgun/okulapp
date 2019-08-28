/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import com.okulapp.forms.controls.BaseFormControl;
import com.okulapp.forms.controls.InputCheckBoxControl;
import com.okulapp.forms.controls.InputCodeMirrorFormControl;
import com.okulapp.forms.controls.InputDateFormControl;
import com.okulapp.forms.controls.InputDoubleFormControl;
import com.okulapp.forms.controls.InputImage;
import com.okulapp.forms.controls.InputIntegerFormControl;
import com.okulapp.forms.controls.InputMaskFormControl;
import com.okulapp.forms.controls.InputSelectManyDbFormControl;
import com.okulapp.forms.controls.InputSelectManyFormControl;
import com.okulapp.forms.controls.InputSelectOneDbFormControl;
import com.okulapp.forms.controls.InputSelectOneFormControl;
import com.okulapp.forms.controls.InputTextFormControl;

/**
 *
 * @author cihan
 */
public class FormControlFactory {

    public static BaseFormControl createFormControl() {
        return new BaseFormControl();
    }

    public static BaseFormControl createFormControlFromDb(Map<String, Object> rec, String locale) {
        BaseFormControl bfc = null;
        if (rec.get("controllClassName") != null) {
            switch (rec.get("controllClassName").toString()) {
                case "InputTextFormControl":
                    bfc = new InputTextFormControl();
                    break;
                case "InputCheckBoxControl":
                    bfc = new InputCheckBoxControl();
                    break;
                case "InputDateFormControl":
                    bfc = new InputDateFormControl();
                    if (rec.get("pattern") != null) {
                        ((InputDateFormControl) bfc).setPattern(rec.get("pattern").toString());
                    }
                    break;
                case "InputDoubleFormControl":
                    bfc = new InputDoubleFormControl();
                    break;
                case "InputIntegerFormControl":
                    bfc = new InputIntegerFormControl();
                    break;
                case "InputMaskFormControl":
                    bfc = new InputMaskFormControl();
                    if (rec.get("mask") != null) {
                        ((InputMaskFormControl) bfc).setMask(rec.get("mask").toString());
                    }
                    break;
                case "InputSelectOneDbFormControl":
                    bfc = new InputSelectOneDbFormControl();
                    if (rec.get("dbName") != null) {
                        ((InputSelectOneDbFormControl) bfc).setDbName(rec.get("dbName").toString());
                    }
                    if (rec.get("tableName") != null) {
                        ((InputSelectOneDbFormControl) bfc).setTableName(rec.get("tableName").toString());
                    }
                    if (rec.get("itemQuery") != null) {
                        ((InputSelectOneDbFormControl) bfc).setItemQuery(rec.get("itemQuery").toString());
                    }
                    if (rec.get("itemLabelField") != null) {
                        ((InputSelectOneDbFormControl) bfc).setItemLabelField(rec.get("itemLabelField").toString());
                    }
                    if (rec.get("itemValueField") != null) {
                        ((InputSelectOneDbFormControl) bfc).setItemValueField(rec.get("itemValueField").toString());
                    }
                    break;
                case "InputSelectManyDbFormControl":
                    bfc = new InputSelectManyDbFormControl();
                    bfc.set(new ArrayList());
                    bfc.setValue(new ArrayList());
                    if (rec.get("dbName") != null) {
                        ((InputSelectManyDbFormControl) bfc).setDbName(rec.get("dbName").toString());
                    }
                    if (rec.get("tableName") != null) {
                        ((InputSelectManyDbFormControl) bfc).setTableName(rec.get("tableName").toString());
                    }
                    if (rec.get("itemQuery") != null) {
                        ((InputSelectManyDbFormControl) bfc).setItemQuery(rec.get("itemQuery").toString());
                    }
                    if (rec.get("itemLabelField") != null) {
                        ((InputSelectManyDbFormControl) bfc).setItemLabelField(rec.get("itemLabelField").toString());
                    }
                    if (rec.get("itemValueField") != null) {
                        ((InputSelectManyDbFormControl) bfc).setItemValueField(rec.get("itemValueField").toString());
                    }
                    break;
                case "InputSelectOneFormControl":
                    bfc = new InputSelectOneFormControl();
                    if (rec.get("items") != null) {
                        List<SelectItem> items = new ArrayList();
                        List<Object> recItems = new ArrayList();
                        if (rec.get("items") instanceof ArrayList) {
                            recItems = (List<Object>) rec.get("items");
                        } else if (rec.get("items") instanceof String) {
                            for (String item : rec.get("items").toString().split(",")) {
                                recItems.add(item);
                            }
                        }

                        for (Object recItem : recItems) {
                            items.add(new SelectItem(recItem, recItem.toString()));
                        }
                        ((InputSelectOneFormControl) bfc).setItems(items);
                    }
                    break;
                case "InputSelectManyFormControl":
                    bfc = new InputSelectManyFormControl();
                    bfc.setValue(new ArrayList());
                    if (rec.get("items") != null) {
                        List<SelectItem> items = new ArrayList();
                        List<Object> recItems = (List<Object>) rec.get("items");
                        for (Object recItem : recItems) {
                            items.add(new SelectItem(recItem, recItem.toString()));
                        }
                        ((InputSelectManyFormControl) bfc).setItems(items);
                    }
                    break;
                case "InputCodeMirrorFormControl":
                    bfc = new InputCodeMirrorFormControl();
                    if (rec.get("mode") != null) {
                        ((InputCodeMirrorFormControl) bfc).setMode(rec.get("mode").toString());
                    }
                    break;
                case "InputImage":
                    bfc = new InputImage();
                    break;
            }

            if (bfc != null) {
                bfc.setAjaxUpdateFieldName(rec.getOrDefault("ajaxUpdateFieldName", "").toString());
                bfc.setAjaxUpdateFieldQueryFieldName(rec.getOrDefault("ajaxUpdateFieldQueryFieldName", "").toString());
                bfc.setGeneratedId(rec.getOrDefault("crudFormCode", "crudForm").toString().concat("_").concat(rec.getOrDefault("fieldName", "fieldName").toString()));
                bfc.setDefaultValue(rec.get("defaultValue"));
                bfc.setFieldName(rec.get("fieldName").toString());
                bfc.setName(rec.get("name").toString());
                bfc.setRequired((Boolean) rec.getOrDefault("required", false));
                if (bfc.getValue() == null) {
                    bfc.setValue(rec.get("defaultValue"));
                }
                if (rec.get("showList") != null) {
                    bfc.setShowList((Boolean) rec.get("showList"));
                }
                if (locale != null) {
                    if ("tr_TR".equals(locale)) {
                        if (rec.get("label") != null) {
                            bfc.setLabel(rec.get("label").toString());
                        }
                    } else if ("en_US".equals(locale)) {
                        if (rec.get("labelEn") != null) {
                            bfc.setLabel(rec.get("labelEn").toString());
                        }
                    } else {
                        if (rec.get("label") != null) {
                            bfc.setLabel(rec.get("label").toString());
                        }
                    }
                }
            }

        }
        return bfc;
    }
}
