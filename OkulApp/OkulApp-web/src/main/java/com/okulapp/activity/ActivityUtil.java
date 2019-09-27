/*
 * 
 * 
 * 
 */
package com.okulapp.activity;

import com.mongodb.QueryBuilder;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.notify.NotifyUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class ActivityUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");

    public static Map<String, Object> getDailyActivity(MyDataSBLocal myDataSB, ObjectId selectedClass, String activityType, String selectedMeal, String userName) {
        QueryBuilder qb = QueryBuilder
                .start("date").is(sdfDate.format(new Date()))
                .and("class").is(selectedClass)
                .and("activityType").is(activityType);

        if ("lunch".equals(activityType)) {
            qb.and("meal").is(selectedMeal);
        }

        Map<String, Object> dailyActivity = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "dailyActivity", qb
                .get().toMap());
        if (dailyActivity == null) {
            dailyActivity = new HashMap();
            dailyActivity.put("_id", new ObjectId());
            dailyActivity.put("date", sdfDate.format(new Date()));
            dailyActivity.put("class", selectedClass);
            dailyActivity.put("activityType", activityType);
            if ("lunch".equals(activityType)) {
                dailyActivity.put("meal", selectedMeal);
            }
            dailyActivity.put("students", new ArrayList());
            dailyActivity.put("creator", userName);
            dailyActivity.put("createDate", new Date());
            myDataSB.getAdvancedDataAdapter().create(myDataSB.getDbName(), "dailyActivity", dailyActivity);
        }
        return dailyActivity;
    }

    private static void sendNotificationsToParents(MyDataSBLocal myDataSB, Map<String, Object> senderUser, Map<String, Object> student, String head, String status, String messageType) {
        CrudListResult listOfParents = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "studentParent", QueryBuilder.start("student").is(student.get("_id")).get().toMap(), null);
        if (listOfParents != null && !listOfParents.isEmpty()) {
            List<String> receivers = new ArrayList();
            List<String> receiversNS = new ArrayList();
            for (Map<String, Object> parent : listOfParents) {
                receivers.add(parent.get("email").toString());
                receiversNS.add(parent.get("nameSurname").toString());
            }
            String notificationMessage = student.get("nameSurname").toString().concat(" ").concat(sdf.format(new Date())).concat(" tarihinde ").concat(head).concat(" ").concat(status);
            NotifyUtil.insertNotifyMessage(myDataSB, messageType, senderUser, receivers, receiversNS, notificationMessage, new ArrayList(), new ArrayList());
        }
    }

    private static void setStatusOfStudent(MyDataSBLocal myDataSB, ObjectId selectedClass, String activityType, String selectedMeal, String userName, Map<String, Object> student, String status) {
        Map<String, Object> di = getDailyActivity(myDataSB, selectedClass, activityType, selectedMeal, userName);
        List<Map<String, Object>> students = (List<Map<String, Object>>) di.get("students");
        Map<String, Object> rec = new HashMap();
        rec.put("studentId", student.get("_id"));
        rec.put("nameSurname", student.get("nameSurname"));
        rec.put("status", status);
        Map<String, Object> toRemoveRec = null;
        for (Map<String, Object> finds : students) {
            if (finds.get("studentId").equals(rec.get("studentId"))) {
                toRemoveRec = finds;
            }
        }
        if (toRemoveRec != null) {
            students.remove(toRemoveRec);
        }
        students.add(rec);
        di.put("students", students);
        myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "dailyActivity", di);
    }

    public static void setMealStatusOfStudent(MyDataSBLocal myDataSB, ObjectId selectedClass, String activityType, String selectedMeal, String userName, Map<String, Object> student, String status, Map<String, Object> user) {
        setStatusOfStudent(myDataSB, selectedClass, activityType, selectedMeal, userName, student, status);
        sendNotificationsToParents(myDataSB, user, student, selectedMeal.concat(" öğününde yemeğini ").concat(status), status, activityType);
    }

    public static void setSleepStatusOfStudent(MyDataSBLocal myDataSB, ObjectId selectedClass, String activityType, String selectedMeal, String userName, Map<String, Object> student, String status, Map<String, Object> user) {
        setStatusOfStudent(myDataSB, selectedClass, activityType, selectedMeal, userName, student, status);
        sendNotificationsToParents(myDataSB, user, student, "Öğlen Uykusunu", status, activityType);
    }

    public static void setEmotionStatusOfStudent(MyDataSBLocal myDataSB, ObjectId selectedClass, String activityType, String selectedMeal, String userName, Map<String, Object> student, String status, Map<String, Object> user) {
        setStatusOfStudent(myDataSB, selectedClass, activityType, selectedMeal, userName, student, status);
        String messageType = "happy";
        if ("Mutlu".equals(status)) {
            messageType = "happy";
        } else if ("Durgun".equals(status)) {
            messageType = "sad";
        } else if ("Üzgün".equals(status)) {
            messageType = "cry";
        }
        sendNotificationsToParents(myDataSB, user, student, "Duygu Durumu", status, messageType);
    }

}
