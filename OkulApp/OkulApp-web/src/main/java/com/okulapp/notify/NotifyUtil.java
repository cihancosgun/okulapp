/*
 * 
 * 
 * 
 */
package com.okulapp.notify;

import com.mongodb.BasicDBObject;
import com.okulapp.data.okul.MyDataSBLocal;
import java.util.ArrayList;
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
        return rec;
    }
}
