/*
 * 
 * 
 * 
 */
package com.okulapp.inspection;

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
public class InspectionUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");

    public static void sendNotificationsToParents(MyDataSBLocal myDataSB, Map<String, Object> senderUser, Map<String, Object> student, boolean comeIn) {
        CrudListResult listOfParents = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "studentParent", QueryBuilder.start("student").is(student.get("_id")).get().toMap(), null);
        if (listOfParents != null && !listOfParents.isEmpty()) {
            List<String> receivers = new ArrayList();
            List<String> receiversNS = new ArrayList();
            for (Map<String, Object> parent : listOfParents) {
                receivers.add(parent.get("email").toString());
                receiversNS.add(parent.get("nameSurname").toString());
            }
            String notificationMessage = student.get("nameSurname").toString().concat(" ").concat(sdf.format(new Date())).concat(" tarihinde okulumuza ").concat(comeIn ? "geldi" : "gelmedi");
            NotifyUtil.insertNotifyMessage(myDataSB, "inspection", senderUser, receivers, receiversNS, notificationMessage, new ArrayList(), new ArrayList());
        }
    }

    public static Map<String, Object> getDailyInspection(MyDataSBLocal myDataSB, String creatorUserName) {
        Map<String, Object> dailyInspection = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "dailyInspection", QueryBuilder.start("date").is(sdfDate.format(new Date())).get().toMap());
        if (dailyInspection == null) {
            dailyInspection = new HashMap();
            dailyInspection.put("_id", new ObjectId());
            dailyInspection.put("date", sdfDate.format(new Date()));
            dailyInspection.put("students", new ArrayList());
            dailyInspection.put("creator", creatorUserName);
            dailyInspection.put("createDate", new Date());
            myDataSB.getAdvancedDataAdapter().create(myDataSB.getDbName(), "dailyInspection", dailyInspection);
        }
        return dailyInspection;
    }

    public static void setInspectionStatusOfStudent(MyDataSBLocal myDataSB, String userName, Map<String, Object> user, Map<String, Object> student, boolean comeIn) {
        Map<String, Object> di = getDailyInspection(myDataSB, userName);
        List<Map<String, Object>> students = (List<Map<String, Object>>) di.get("students");
        Map<String, Object> rec = new HashMap();
        rec.put("studentId", student.get("_id"));
        rec.put("nameSurname", student.get("nameSurname"));
        rec.put("comeInStatus", comeIn);
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
        myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "dailyInspection", di);
        sendNotificationsToParents(myDataSB, user, student, comeIn);
    }
}
