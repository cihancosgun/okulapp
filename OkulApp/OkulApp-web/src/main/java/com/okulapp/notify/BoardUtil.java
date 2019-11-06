/*
 * 
 * 
 * 
 */
package com.okulapp.notify;

import com.mongodb.QueryBuilder;
import com.okulapp.api.ApiResource;
import com.okulapp.data.MongoDataAdapter;
import com.okulapp.data.okul.MyDataSBLocal;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class BoardUtil {

    private static byte[] inputStreamToByteArray(InputStream initialStream) {
        try {
            byte[] targetArray = new byte[initialStream.available()];
            initialStream.read(targetArray);
            return targetArray;
        } catch (IOException ex) {
            Logger.getLogger(ApiResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static InputStream handleMessageTypeIcon(String messageType) {
        InputStream is = BoardUtil.class.getClassLoader().getResourceAsStream(messageType.concat(".png"));
        if (is != null) {
            return is;
        } else {
            return BoardUtil.class.getClassLoader().getResourceAsStream("board.png");
        }
    }

    public static List<Map<String, Object>> getBoardOfUser(MyDataSBLocal myDataSB, String userName) {
        return getBoardOfUser(myDataSB, userName, false, false);
    }

    public static List<Map<String, Object>> getBoardOfUser(MyDataSBLocal myDataSB, String userName, boolean includeMessageTypeIcon, boolean includeFirstImageThumbnail) {
        List<Map<String, Object>> myBoard = null;
        MongoDataAdapter adapter = (MongoDataAdapter) myDataSB.getAdvancedDataAdapter().getSelectedDataAdapter();
        QueryBuilder filter = QueryBuilder.start("deleted").is(false)
                .and("users").in(Arrays.asList(userName));
        QueryBuilder projection = QueryBuilder.start();

        myBoard = adapter.getSortedPagedList(myDataSB.getDbName(), "notifications", filter.get().toMap(), projection.get().toMap(), 0, 1000, QueryBuilder.start("startDate").is(-1).get().toMap());
        for (Map<String, Object> rec : myBoard) {
            if (includeMessageTypeIcon) {
                String b64 = Base64.encodeBase64String(inputStreamToByteArray(handleMessageTypeIcon(rec.get("messageType").toString())));
                rec.put("messageTypeB64", "data:image/png;base64,".concat(b64));
            }
            List<ObjectId> thumbFileIds = (List<ObjectId>) rec.get("thumbFileIds");
            if (includeFirstImageThumbnail && thumbFileIds != null && !thumbFileIds.isEmpty()) {
                byte[] bytes = (byte[]) myDataSB.getFileUpDownManager().downloadFile(thumbFileIds.get(0)).get("bytes");
                String firstImageB64 = Base64.encodeBase64String(bytes);
                rec.put("firstImageB64", "data:image/png;base64,".concat(firstImageB64));
            }
            List<ObjectId> fileIds = (List<ObjectId>) rec.getOrDefault("fileIds", new ArrayList());
            if (fileIds != null && !fileIds.isEmpty() && fileIds.size() > 4) {
                rec.put("fileIdsLimited", fileIds.subList(0, 4));
            } else {
                rec.put("fileIdsLimited", fileIds);
            }
        }
        return myBoard;
    }

    public static void setReaded(MyDataSBLocal myDataSB, List<Map<String, Object>> unreadedBoard, String userName) {
        for (Map<String, Object> currentNotify : unreadedBoard) {
            List<String> readedUsers = (List<String>) currentNotify.get("readedUsers");
            if (!readedUsers.contains(userName)) {
                readedUsers.add(userName);
                currentNotify.put("readedUsers", readedUsers);
                myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "notifications", currentNotify);
            }
        }
    }

    public static List<Map<String, Object>> getUnreadedBoard(MyDataSBLocal myDataSB, String userName) {
        MongoDataAdapter adapter = (MongoDataAdapter) myDataSB.getAdvancedDataAdapter().getSelectedDataAdapter();
        List<Map<String, Object>> myUnreadedBoard = null;
        QueryBuilder projection = QueryBuilder.start();
        QueryBuilder filter = QueryBuilder.start("deleted").is(false)
                .and("users").in(Arrays.asList(userName)).
                and("readedUsers").notIn(Arrays.asList(userName));
        myUnreadedBoard = adapter.getSortedPagedList(myDataSB.getDbName(), "notifications", filter.get().toMap(), projection.get().toMap(), 0, 1000, QueryBuilder.start("startDate").is(-1).get().toMap());
        return myUnreadedBoard;
    }

}
