/*
 * 
 * 
 * 
 */
package com.okulapp.notify;

import com.mongodb.BasicDBObject;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.expopush.ExpoPushNotificationUtil;
import com.okulapp.security.SecurityUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class NotifyUtil {

    public static BasicDBObject insertNotifyMessage(MyDataSBLocal myDataSB, String messageType, Map<String, Object> senderUser,
            List<String> receivers,
            List<String> receiversNS,
            String notificationMessage,
            List<ObjectId> fileIds, List<ObjectId> thumbFileIds) {
        if (Arrays.asList("board", "event", "remind").contains(messageType)) {
            if (!receivers.contains(senderUser.get("email").toString())) {
                receivers.add(senderUser.get("email").toString());
            }
            if (!receivers.containsAll(SecurityUtil.getAdminUsersEmails(myDataSB))) {
                receivers.addAll(SecurityUtil.getAdminUsersEmails(myDataSB));
            }
        }
        BasicDBObject rec = new BasicDBObject();
        rec.put("_id", new ObjectId());
        rec.put("senderEmail", senderUser.get("email"));
        rec.put("senderNameSurname", senderUser.get("nameSurname"));
        rec.put("users", receivers);
        rec.put("usersNS", receiversNS);
        rec.put("readedUsers", new ArrayList());
        rec.put("messageType", messageType);
        rec.put("message", notificationMessage);
        rec.put("fileIds", fileIds);
        rec.put("thumbFileIds", thumbFileIds);
        rec.put("startDate", new Date());
        rec.put("deleted", false);
        myDataSB.getAdvancedDataAdapter().create(myDataSB.getDbName(), "notifications", rec);
        ExpoPushNotificationUtil.sendPushNotificationToQueue(receivers, "Bildirim", notificationMessage);
        return rec;
    }

    public static BasicDBObject sendNotificationToAllUser(MyDataSBLocal myDataSB, Map<String, Object> senderUser, String messageType, String notificationMessage) {
        CrudListResult users = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "users", new BasicDBObject(), new BasicDBObject());
        List<String> receivers = new ArrayList();
        for (Map<String, Object> user : users) {
            receivers.add(user.get("login").toString());
        }
        return insertNotifyMessage(myDataSB, messageType, senderUser, receivers, receivers, notificationMessage, new ArrayList(), new ArrayList());
    }
}
