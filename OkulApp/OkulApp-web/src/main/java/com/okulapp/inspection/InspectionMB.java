/*
 * 
 * 
 * 
 */
package com.okulapp.inspection;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.dispatcher.DispatcherMB;
import com.okulapp.forms.CrudMB;
import com.okulapp.notify.NotificationMB;
import com.okulapp.security.SecurityMB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "inspectionMB")
@SessionScoped
public class InspectionMB implements Serializable {

    @EJB
    MyDataSBLocal myDataSB;

    @Inject
    CrudMB crudMB;

    private @Inject
    SecurityMB securityMB;

    private @Inject
    DispatcherMB dispatcherMB;

    private @Inject
    NotificationMB notificationMB;

    private final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private List<Map<String, Object>> branches;
    private ObjectId selectedBranch;
    private ObjectId selectedClass;
    private List<Map<String, Object>> listClasses;
    private List<Map<String, Object>> listStudents;
    private Map<String, Object> dailyInspection;

    /**
     * Creates a new instance of InspectionMB
     */
    public InspectionMB() {
    }

    public Map<String, Object> getDailyInspection() {
        dailyInspection = InspectionUtil.getDailyInspection(myDataSB, securityMB.getRemoteUserName());
        return dailyInspection;
    }

    public ObjectId getSelectedClass() {
        return selectedClass;
    }

    @PostConstruct
    public void init() {
        branches = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "branch", new BasicDBObject(), new BasicDBObject());
        if (branches != null && securityMB.getLoginUser() != null) {
            setSelectedBranch((ObjectId) securityMB.getLoginUser().getOrDefault("branch", branches.get(0).get("_id")));
            refreshClasses();
        }
    }

    public List<Map<String, Object>> getBranches() {
        return branches;
    }

    public void setBranches(List<Map<String, Object>> branches) {
        this.branches = branches;
    }

    public ObjectId getSelectedBranch() {
        return selectedBranch;
    }

    public void setDailyInspection(Map<String, Object> dailyInspection) {
        this.dailyInspection = dailyInspection;
    }

    public void setSelectedBranch(ObjectId selectedBranch) {
        this.selectedBranch = selectedBranch;
        if (selectedBranch != null) {
            refreshClasses();
        }
    }

    public List<Map<String, Object>> getListClasses() {
        return listClasses;
    }

    public void setListClasses(List<Map<String, Object>> listClasses) {
        this.listClasses = listClasses;
    }

    public List<Map<String, Object>> getListStudents() {
        return listStudents;
    }

    public void setListStudents(List<Map<String, Object>> listStudents) {
        this.listStudents = listStudents;
    }

    public void refreshClasses() {
        QueryBuilder qb = QueryBuilder.start();
        if (selectedBranch != null) {
            qb = QueryBuilder.start("branch").is(selectedBranch);
        }
        if ("teacher".equals(securityMB.getLoginUserRole())) {
            qb.and("teacher").is(securityMB.getLoginUser().get("_id"));
        }
        setListClasses(myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "classes", qb.get().toMap(), null));
    }

    public void refreshStudents() {
        QueryBuilder qb = QueryBuilder.start();
        if (selectedClass != null) {
            qb = QueryBuilder.start("class").is(selectedClass);
        }
        setListStudents(myDataSB.getAdvancedDataAdapter().getSortedList(myDataSB.getDbName(), "student", qb.get().toMap(), null, QueryBuilder.start("schoolNo").is(1).get().toMap()));
    }

    public void setSelectedClass(ObjectId selectedClass) {
        this.selectedClass = selectedClass;
        if (selectedClass != null) {
            refreshStudents();
        }
    }

    public boolean isStudentIn(Map<String, Object> student) {
        Map<String, Object> rec = new HashMap();
        rec.put("studentId", student.get("_id"));
        rec.put("nameSurname", student.get("nameSurname"));
        rec.put("comeInStatus", true);
        Map<String, Object> di = getDailyInspection();
        List<Map<String, Object>> students = (List<Map<String, Object>>) di.get("students");
        return students.contains(rec);
    }

    public boolean isStudentNotIn(Map<String, Object> student) {
        Map<String, Object> rec = new HashMap();
        rec.put("studentId", student.get("_id"));
        rec.put("nameSurname", student.get("nameSurname"));
        rec.put("comeInStatus", false);
        Map<String, Object> di = getDailyInspection();
        List<Map<String, Object>> students = (List<Map<String, Object>>) di.get("students");
        return students.contains(rec);
    }

    public void setInspectionStatusOfStudent(Map<String, Object> student, boolean comeIn) {
        InspectionUtil.setInspectionStatusOfStudent(myDataSB, securityMB.getRemoteUserName(), securityMB.getLoginUser(), student, comeIn);
    }

}
