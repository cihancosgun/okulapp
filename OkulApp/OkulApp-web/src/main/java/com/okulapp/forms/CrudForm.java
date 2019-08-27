/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.data.AdvancedDataAdapter;
import com.okulapp.forms.controls.BaseFormControl;
import com.okulapp.security.CrudSecurityRole;

/**
 *
 * @author cihan
 */
public class CrudForm extends Form {

    private List<CrudSecurityRole> crudSecurityRoles;
    private List<BaseFormControl> formControls;
    private List<ListField> listFields;

    private String dbName;
    private String tableName;
    private AdvancedDataAdapter ada;
    private Boolean newMode = true;
    private Object _id = null;
    
    private String crudFormCode;

    private Map<String, Integer> mapFormControlsIndex;

    private CrudForm() {
        setPageUrl("/pages/crudPage.xhtml");
    }

    public String getCrudFormCode() {
        return crudFormCode;
    }

    public List<CrudSecurityRole> getCrudSecurityRoles() {
        return crudSecurityRoles;
    }

    public Map<String, Integer> getMapFormControlsIndex() {
        return mapFormControlsIndex;
    }

    public void setCrudFormCode(String crudFormCode) {
        this.crudFormCode = crudFormCode;
    }

    public void setCrudSecurityRoles(List<CrudSecurityRole> crudSecurityRoles) {
        this.crudSecurityRoles = crudSecurityRoles;
    }

    public List<BaseFormControl> getFormControls() {
        return formControls;
    }

    public void setFormControls(List<BaseFormControl> formControls) {
        this.formControls = formControls;
    }

    public String getTableName() {
        return tableName;
    }

    public void setMapFormControlsIndex(Map<String, Integer> mapFormControlsIndex) {
        this.mapFormControlsIndex = mapFormControlsIndex;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public CrudListResult getList(Map<String, Object> filter, Map<String, Object> projection) {
        return ada.getList(dbName, tableName, filter, projection);
    }

    public CrudListResult getPagedList(Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize) {
        return ada.getPagedList(dbName, tableName, filter, projection, page, pageSize);
    }

    public CrudListResult getSortedPagedList(Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize, Map<String, Object> sort) {
        return ada.getSortedPagedList(dbName, tableName, filter, projection, page, pageSize, sort);
    }

    public Map<String, Object> create(Map<String, Object> rec) {
        return ada.create(dbName, tableName, rec);
    }

    public Map<String, Object> read(Map<String, Object> find) {
        return ada.read(dbName, tableName, find);
    }

    public Map<String, Object> update(Map<String, Object> rec) {
        return ada.update(dbName, tableName, rec);
    }

    public Map<String, Object> delete(Map<String, Object> rec) {
        return ada.delete(dbName, tableName, rec);
    }

    public List<ListField> getListFields() {
        return listFields;
    }

    public void setListFields(List<ListField> listFields) {
        this.listFields = listFields;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public AdvancedDataAdapter getAda() {
        return ada;
    }

    public void setAda(AdvancedDataAdapter ada) {
        this.ada = ada;
    }

    public Boolean getNewMode() {
        return newMode;
    }

    public void setNewMode(Boolean newMode) {
        this.newMode = newMode;
    }

    public Map<String, Object> fillCrudObjectWithFormControls() {
        Map<String, Object> data = new HashMap();
        if (!newMode && _id != null) {
            data.put("_id", _id);
        }
        for (BaseFormControl fc : formControls) {
            Object value = fc.get();
            if (fc.get() instanceof Object[]) {
                value = new ArrayList();
                for (Object str : (Object[]) fc.get()) {
                    ((ArrayList) value).add(str);
                }

            }
            data.put(fc.getFieldName(), value);
        }
        return data;
    }

    public void save() {
        Map<String, Object> rec = fillCrudObjectWithFormControls();
        if (newMode) {
            create(rec);
        } else {
            update(rec);
        }
    }

    public Object getId() {
        return _id;
    }

    public void setId(Object _id) {
        this._id = _id;
    }

    public static class Builder {

        CrudForm cf;
        private final AdvancedDataAdapter ada;
        private final String dbName;
        private final String locale;
        private static final String formTable = "crudforms";
        private static final String fieldsTable = "formFields";
        private static final String CRUD_FORM_CODE = "crudFormCode";
        private static final String TITLE_EN = "titleEn";
        private static final String EN_US = "en_US";
        private static final String TITLE = "title";
        private static final String TR_TR = "tr_TR";
        private static final String TABLE_NAME = "tableName";
        private static final String ORDER = "order";
        private String crudFormCode;

        public Builder(AdvancedDataAdapter ada, String dbName, String locale) {
            cf = new CrudForm();
            this.ada = ada;
            this.dbName = dbName;
            this.locale = locale;
        }

        public Builder createCrudFromFromDb(String crudFormCode) {
            this.crudFormCode = crudFormCode;
            cf.setCrudFormCode(crudFormCode);
            Map<String, Object> find = new HashMap();
            find.put(CRUD_FORM_CODE, crudFormCode);
            cf.setAda(ada);
            cf.setDbName(dbName);
            cf.setListFields(new ArrayList());
            Map<String, Object> rec = ada.read(dbName, formTable, find);
            if (rec.get(TABLE_NAME) != null) {
                cf.setTableName(rec.get(TABLE_NAME).toString());
            }
            if (TR_TR.equals(locale)) {
                if (rec.get(TITLE) != null) {
                    cf.setTitle(rec.get(TITLE).toString());
                }
            } else if (EN_US.equals(locale)) {
                if (rec.get(TITLE_EN) != null) {
                    cf.setTitle(rec.get(TITLE_EN).toString());
                }
            } else {
                if (rec.get(TITLE) != null) {
                    cf.setTitle(rec.get(TITLE).toString());
                }
            }

            return this;
        }

        public Builder createFormControls() {
            List<BaseFormControl> fctrls = getFormControls();
            cf.setMapFormControlsIndex(new HashMap());
            int idx = 0;
            for (BaseFormControl ctrl : fctrls) {
                cf.getMapFormControlsIndex().put(ctrl.getGeneratedId(), idx);
                if (ctrl.getShowList()) {
                    try {
                        BaseFormControl cloneCtrl = (BaseFormControl) ctrl.clone();
                        cloneCtrl.set(null);
                        cloneCtrl.setValue(null);
                        cf.getListFields().add(new ListField(cloneCtrl.getFieldName(), cloneCtrl.getLabel(), cloneCtrl));
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(CrudForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                idx++;
            }
            cf.setFormControls(fctrls);
            return this;
        }

        private List<BaseFormControl> getFormControls() {
            List<BaseFormControl> result = new ArrayList();
            Map<String, Object> filter = new HashMap();
            Map<String, Object> sort = new HashMap();
            filter.put(CRUD_FORM_CODE, crudFormCode);
            sort.put(ORDER, 1);
            CrudListResult crl = ada.getSortedPagedList(dbName, fieldsTable, filter, null, 0, 1000, sort);
            if (crl.getTotalRecordCount() > 0) {
                for (Map<String, Object> rec : crl) {
                    BaseFormControl baseFormControl = FormControlFactory.createFormControlFromDb(rec, locale);
                    if (baseFormControl != null) {
                        result.add(baseFormControl);
                    } else {
                        System.out.println("ERROR : ".concat(rec.toString()).concat(" control not found."));
                    }
                }
            }
            return result;
        }

        public CrudForm build() {
            return cf;
        }
    }

}
