/*
 * 
 * 
 * 
 */
package com.okulapp.security;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.data.okul.MyDataSBLocal;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class SecurityUtil {

    public static ObjectId getBranchOfUser(MyDataSBLocal myDataSB, String email) {
        Map<String, Object> branches = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "branch", new BasicDBObject());
        Map<String, Object> user = getUserFromEmail(myDataSB, email);
        if (branches != null && user != null) {
            return (ObjectId) user.getOrDefault("branch", branches.get("_id"));
        } else {
            return null;
        }
    }

    public static String getUserRoleFromUserTable(MyDataSBLocal myDataSB, String email) {
        Map<String, Object> userRecord = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "users", QueryBuilder.start("login").is(email).get().toMap());
        if (userRecord == null) {
            return "";
        }
        List<String> groups = (List<String>) userRecord.get("groups");
        if (groups == null) {
            return "";
        }
        return groups.get(0);
    }

    public static Map<String, Object> getUserFromEmail(MyDataSBLocal myDataSB, String email) {
        Map<String, Object> user = null;
        if (email != null) {
            String role = getUserRoleFromUserTable(myDataSB, email);
            if ("admin".equals(role)) {
                user = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "stuff", QueryBuilder.start("email").is(email).get().toMap());
            } else if ("teacher".equals(role)) {
                user = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "teachers", QueryBuilder.start("email").is(email).get().toMap());
            } else if ("stuff".equals(role)) {
                user = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "stuff", QueryBuilder.start("email").is(email).get().toMap());
            } else if ("parent".equals(role)) {
                user = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "studentParent", QueryBuilder.start("email").is(email).get().toMap());
            }
        }
        return user;
    }
}
