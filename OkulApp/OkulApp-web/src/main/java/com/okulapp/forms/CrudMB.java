/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.forms;

import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.dispatcher.DispatcherMB;
import com.okulapp.forms.controls.BaseFormControl;
import com.okulapp.forms.controls.InputSelectOneDbFormControl;
import com.okulapp.texts.TextBundlerSMB;
import com.okulapp.navigation.NavigationMB;
import com.okulapp.security.SecurityMB;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.forms.controls.InputImage;
import com.okulapp.forms.controls.InputSelectManyDbFormControl;
import com.okulapp.forms.controls.InputSelectManyFormControl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import org.bson.Document;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author cihan
 */
@Named(value = "crudMB")
@SessionScoped
public class CrudMB implements Serializable {

    private @Inject
    DispatcherMB dispatcherMB;

    private @Inject
    SecurityMB securityMB;

    private @Inject
    NavigationMB navigationMB;

    private @Inject
    TextBundlerSMB textBundlerSMB;

    @EJB
    MyDataSBLocal myDataSB;

    private static final String CRUD_FORM = "/dialogs/crudForm.xhtml";
    private static final String CRUD_PAGE = "/pages/crudPage.xhtml";
    private final List<String> userForms = Arrays.asList("stuff", "teachers", "studentParent");

    private String id;

    private CrudForm crudForm;

    private LazyDataModel<Map<String, Object>> lazyDataModel;

    private Map<String, List<Map<String, Object>>> selectItemCache = new HashMap();

    private Map<String, Object> selectItemValuesCache = new HashMap();

    private boolean showFilter = false;

    public CrudMB() {

    }

    @PostConstruct
    public void init() {
        id = UUID.randomUUID().toString();
    }

    public void showCrudPage(String crudFormCode) {
        Form currentForm = new CrudForm.Builder(myDataSB.getAdvancedDataAdapter(), myDataSB.getDbName(), textBundlerSMB.getLoceleStr())
                .createCrudFromFromDb(crudFormCode)
                .createFormControls()
                .build();
        dispatcherMB.setCurrentPage(currentForm);
        if (currentForm instanceof CrudForm) {
            CrudForm cf = (CrudForm) currentForm;
            cf.setAda(myDataSB.getAdvancedDataAdapter());
            cf.setDbName(myDataSB.getDbName());
            setCrudForm(cf);
            setLazyDataModel(new CrudDataTableLazyDataModel(cf));
        }
    }

    public void showCrudFormWithNewMode(String crudFormCode) {
        Form currentForm = new CrudForm.Builder(myDataSB.getAdvancedDataAdapter(), myDataSB.getDbName(), textBundlerSMB.getLoceleStr())
                .createCrudFromFromDb(crudFormCode)
                .createFormControls()
                .build();
        if (currentForm instanceof CrudForm) {
            CrudForm cf = (CrudForm) currentForm;
            cf.setAda(myDataSB.getAdvancedDataAdapter());
            cf.setDbName(myDataSB.getDbName());
            setCrudForm(cf);
            setLazyDataModel(new CrudDataTableLazyDataModel(cf));
            crudForm.setPageUrl(CRUD_FORM);
            dispatcherMB.setCurrentPage(crudForm);
            this.newRecord();
        }
    }

    public CrudForm getCrudForm() {
        return crudForm;
    }

    public void setCrudForm(CrudForm crudForm) {
        this.crudForm = crudForm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void newRecord() {
        crudForm.setNewMode(true);
        for (BaseFormControl formControl : crudForm.getFormControls()) {
            if (formControl.getDefaultValue() != null && !(formControl instanceof InputSelectManyDbFormControl || formControl instanceof InputSelectManyFormControl)) {
                formControl.setValue(formControl.getDefaultValue());
            }
        }
        crudForm.setPageUrl(CRUD_FORM);
        dispatcherMB.setCurrentPage(crudForm);
    }

    public void saveRecord() {
        Map<String, Object> record = crudForm.fillCrudObjectWithFormControls();
        if (crudForm.getNewMode()) {
            crudForm.create(record);
        } else {
            crudForm.update(record);
        }

        if (crudForm instanceof CrudForm) {
            CrudForm cf = (CrudForm) crudForm;
            if (userForms.contains(cf.getCrudFormCode())) {
                securityMB.syncUser(record, cf);
            }
        }

        crudForm.setPageUrl(CRUD_PAGE);
        dispatcherMB.setCurrentPage(crudForm);
    }

    public void cancelRecord() {
        crudForm.setPageUrl(CRUD_PAGE);
        dispatcherMB.setCurrentPage(crudForm);
    }

    public void editRecord(Map<String, Object> rec) {
        crudForm.setNewMode(false);
        rec = crudForm.read(rec);//lazy to eager
        crudForm.setId(rec.get("_id"));
        for (BaseFormControl formControl : crudForm.getFormControls()) {
            formControl.setValue(rec.get(formControl.getFieldName()));
        }
        crudForm.setPageUrl(CRUD_FORM);
        dispatcherMB.setCurrentPage(crudForm);
    }

    public void copyRecord(Map<String, Object> rec) {
        crudForm.setNewMode(true);
        rec = crudForm.read(rec);//lazy to eager
        rec.remove("_id");
        crudForm.setId(null);
        for (BaseFormControl formControl : crudForm.getFormControls()) {
            formControl.setValue(rec.get(formControl.getFieldName()));
        }
        crudForm.setPageUrl(CRUD_FORM);
        dispatcherMB.setCurrentPage(crudForm);
    }

    public void deleteRecord(Map<String, Object> rec) {
        crudForm.setNewMode(false);
        rec = crudForm.read(rec);//lazy to eager
        crudForm.setId(rec.get("_id"));
        crudForm.delete(rec);

        if (crudForm instanceof CrudForm) {
            CrudForm cf = (CrudForm) crudForm;
            if (userForms.contains(cf.getCrudFormCode())) {
                securityMB.deleteUser(rec, cf);
            }
        }
    }

    public List<Map<String, Object>> getList() {
        Map<String, Object> projection = new HashMap();
        for (ListField listField : crudForm.getListFields()) {
            projection.put(listField.getFieldName(), true);
        }
        CrudListResult clr = crudForm.getPagedList(new HashMap(), projection, 1, 10);
        return clr;
    }

    public void listPage(PageEvent pageEvent) {
        pageEvent.getPage();
    }

    public LazyDataModel<Map<String, Object>> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Map<String, Object>> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Map<String, List<Map<String, Object>>> getSelectItemCache() {
        return selectItemCache;
    }

    public void setSelectItemCache(Map<String, List<Map<String, Object>>> selectItemCache) {
        this.selectItemCache = selectItemCache;
    }

    public List<Map<String, Object>> getSelectItemsFromCacheDB(String controlName, String dbName, String tableName, String itemQuery, String itemValueField, String itemLabelField) {
        Map<String, Object> filter = new HashMap();
        Map<String, Object> sort = new HashMap();
        Map<String, Object> projection = new HashMap();
        if (itemQuery == null || itemQuery.isEmpty()) {
            itemQuery = "{}";
        }
        filter = myDataSB.getAdvancedDataAdapter().getMapFromString(itemQuery);
        sort.put(itemLabelField, 1);
        return myDataSB.getAdvancedDataAdapter().getSortedList(dbName, tableName, filter, projection, sort);
    }

    public Map<String, Object> getSelectItemValuesCache() {
        return selectItemValuesCache;
    }

    public void setSelectItemValuesCache(Map<String, Object> selectItemValuesCache) {
        this.selectItemValuesCache = selectItemValuesCache;
    }

    public Object getSelectItemValuesCacheFromDb(BaseFormControl selectOneDbFormControl, Object itemValueFieldValue) {
        if (selectOneDbFormControl instanceof InputSelectOneDbFormControl && itemValueFieldValue != null) {
            InputSelectOneDbFormControl inputSelectOneDbFormControl = (InputSelectOneDbFormControl) selectOneDbFormControl;
            String key = inputSelectOneDbFormControl.getDbName().concat("_")
                    .concat(inputSelectOneDbFormControl.getTableName()).concat("_")
                    .concat(inputSelectOneDbFormControl.getItemLabelField()).concat("_")
                    .concat(itemValueFieldValue.toString());
            if (selectItemValuesCache.get(key) == null) {
                Map<String, Object> find = new HashMap();
                if (itemValueFieldValue instanceof String && ObjectId.isValid(itemValueFieldValue.toString())) {
                    itemValueFieldValue = new ObjectId(itemValueFieldValue.toString());
                }
                find.put(inputSelectOneDbFormControl.getItemValueField(), itemValueFieldValue);
                Map<String, Object> result = myDataSB.getAdvancedDataAdapter().read(inputSelectOneDbFormControl.getDbName(), inputSelectOneDbFormControl.getTableName(), find);
                if (result != null) {
                    selectItemValuesCache.put(key, result.get(inputSelectOneDbFormControl.getItemLabelField()));
                }
            }
            return selectItemValuesCache.get(key);
        } else {
            return itemValueFieldValue;
        }
    }

    public boolean isNotSelectableControl(String controlType) {
        return !Arrays.asList("selectOneDb,selectOne,selectManyDb,selectMany".split(",")).contains(controlType);
    }

    public boolean isNotDateControl(String controlType) {
        return !Arrays.asList("date".split(",")).contains(controlType);
    }

    public boolean isShowFilter() {
        return showFilter;
    }

    public void setShowFilter(boolean showFilter) {
        this.showFilter = showFilter;
    }

    public void switchShowFilter() {
        this.showFilter = !showFilter;
    }

    public void refreshDatatable() {
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("contentCmp:crudListForm:crudDataTable");
        if (dataTable != null) {
            dataTable.loadLazyData();
        }
    }

    public void resetFilter() {
        if (crudForm != null && crudForm.getListFields() != null && !crudForm.getListFields().isEmpty()) {
            for (ListField listField : crudForm.getListFields()) {
                listField.getFormControl().setValue(null);
                listField.getFormControl().set(null);
                refreshDatatable();
            }
        }
    }

    public void onSelectOneChanged(AjaxBehaviorEvent event) {
        BaseFormControl fc = crudForm.getFormControls().get(crudForm.getMapFormControlsIndex().get(event.getComponent().getId()));
        if (fc.getValue() != null && fc.getAjaxUpdateFieldName() != null) {
            BaseFormControl fcEffected = (BaseFormControl) crudForm.getFormControls().get(crudForm.getMapFormControlsIndex().get(fc.getAjaxUpdateFieldName().replace("contentCmp:crudFormForm:", "")));
            if (fcEffected != null) {
                Document query = new Document();
                query.put(fc.getAjaxUpdateFieldQueryFieldName(), fc.getValue());
                if (fcEffected instanceof InputSelectOneDbFormControl) {
                    ((InputSelectOneDbFormControl) fcEffected).setItemQuery(query.toJson());
                } else if (fcEffected instanceof InputSelectManyDbFormControl) {
                    ((InputSelectManyDbFormControl) fcEffected).setItemQuery(query.toJson());
                }
            }
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            BaseFormControl fc = crudForm.getFormControls().get(crudForm.getMapFormControlsIndex().get(event.getComponent().getId()));
            if (fc != null) {
                if ("image".equals(fc.getType())) {
                    InputImage inputImage = (InputImage) fc;
                    InputStream is = event.getFile().getInputstream();
                    ObjectId fileId = myDataSB.getFileUpDownManager().uploadFile(is, event.getFile().getFileName());

                    if (fileId != null) {
                        inputImage.set(fileId);
                        inputImage.setValue(fileId);
                        FacesMessage msg = new FacesMessage("Dosya Yüklendi", event.getFile().getFileName());
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(InputImage.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage msg = new FacesMessage("Dosya Yüklenemedi", event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public StreamedContent handleImageFileDownload(ObjectId fileId) {
        if (fileId == null) {
            InputStream is = getClass().getClassLoader().getResourceAsStream("user-profile.png");
            return new DefaultStreamedContent(is, "image/png");
        } else {
            byte[] bytes = myDataSB.getFileUpDownManager().downloadFile(fileId);
            if (bytes != null) {
                return new DefaultStreamedContent(new ByteArrayInputStream(bytes));
            } else {
                return null;
            }
        }

    }
}
