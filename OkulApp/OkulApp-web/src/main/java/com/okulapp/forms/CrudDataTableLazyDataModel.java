/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.forms.controls.InputCheckBoxControl;

/**
 *
 * @author cihan
 */
public class CrudDataTableLazyDataModel extends LazyDataModel<Map<String, Object>> {

    private CrudForm crudForm;

    public CrudDataTableLazyDataModel() {
    }

    public CrudDataTableLazyDataModel(CrudForm crudForm) {
        this.crudForm = crudForm;
    }

    @Override
    public Map<String, Object> getRowData(String rowKey) {
        Map<String, Object> find = new HashMap();
        find.put("_id", rowKey);
        return crudForm.read(find);
    }

    @Override
    public Object getRowKey(Map<String, Object> rec) {
        return rec.get("_id");
    }

    @Override
    public List<Map<String, Object>> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Map<String, Object> projection = new HashMap();
        if (crudForm.getListFields() != null) {
            for (ListField listField : crudForm.getListFields()) {
                projection.put(listField.getFieldName(), true);
            }
        }

        Map<String, Object> sort = new HashMap();
        if (sortField != null) {
            sort.put(sortField, sortOrder.ASCENDING == sortOrder ? 1 : -1);
        }

        Map<String, Object> editedFilters = new HashMap();
        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                editedFilters.put(key, Pattern.compile(value.toString()));
            }
        }

        if (crudForm.getListFields() != null && !crudForm.getListFields().isEmpty()) {
            for (ListField listField : crudForm.getListFields()) {
                if (listField.getFormControl().getValue() != null && !listField.getFormControl().getValue().toString().isEmpty()) {
                    if ("text".equals(listField.getFormControl().getType())) {
                        editedFilters.put(listField.getFieldName(), Pattern.compile(listField.getFormControl().getValue().toString()));
                    } else if ("checkbox".equals(listField.getFormControl().getType())) {
                        if (((InputCheckBoxControl) listField.getFormControl()).get()) {
                            editedFilters.put(listField.getFieldName(), listField.getFormControl().getValue());
                        }
                    } else {
                        editedFilters.put(listField.getFieldName(), listField.getFormControl().getValue());
                    }
                }
            }
        }

        CrudListResult clr = crudForm.getSortedPagedList(editedFilters, projection, first, pageSize, sort);
        this.setRowCount((int) clr.getTotalRecordCount());
        return clr;
    }

    public CrudForm getCrudForm() {
        return crudForm;
    }

    public void setCrudForm(CrudForm crudForm) {
        this.crudForm = crudForm;
    }

}
