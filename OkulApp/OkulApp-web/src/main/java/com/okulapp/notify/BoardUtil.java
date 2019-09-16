/*
 * 
 * 
 * 
 */
package com.okulapp.notify;

import com.mongodb.QueryBuilder;
import com.okulapp.data.MongoDataAdapter;
import com.okulapp.data.okul.MyDataSBLocal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class BoardUtil {

    public static List<Map<String, Object>> getBoardOfUser(MyDataSBLocal myDataSB, String userName) {
        List<Map<String, Object>> myBoard = null;
        MongoDataAdapter adapter = (MongoDataAdapter) myDataSB.getAdvancedDataAdapter().getSelectedDataAdapter();
        QueryBuilder filter = QueryBuilder.start("deleted").is(false)
                .and("users").in(Arrays.asList(userName));
        QueryBuilder projection = QueryBuilder.start();

        myBoard = adapter.getSortedPagedList(myDataSB.getDbName(), "notifications", filter.get().toMap(), projection.get().toMap(), 0, 20, QueryBuilder.start("startDate").is(-1).get().toMap());
        for (Map<String, Object> rec : myBoard) {
            List<ObjectId> fileIds = (List<ObjectId>) rec.getOrDefault("fileIds", new ArrayList());
            if (fileIds != null && !fileIds.isEmpty() && fileIds.size() > 4) {
                rec.put("fileIdsLimited", fileIds.subList(0, 4));
            } else {
                rec.put("fileIdsLimited", fileIds);
            }
        }
        return myBoard;
    }

    public static List<Map<String, Object>> getUnreadedBoard(MyDataSBLocal myDataSB, String userName) {
        MongoDataAdapter adapter = (MongoDataAdapter) myDataSB.getAdvancedDataAdapter().getSelectedDataAdapter();
        List<Map<String, Object>> myUnreadedBoard = null;
        QueryBuilder projection = QueryBuilder.start();
        QueryBuilder filter = QueryBuilder.start("deleted").is(false)
                .and("users").in(Arrays.asList(userName)).
                and("readedUsers").notIn(Arrays.asList(userName));
        myUnreadedBoard = adapter.getSortedPagedList(myDataSB.getDbName(), "notifications", filter.get().toMap(), projection.get().toMap(), 0, 20, QueryBuilder.start("startDate").is(-1).get().toMap());
        return myUnreadedBoard;
    }

}
