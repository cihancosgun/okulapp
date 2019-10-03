/*
 * 
 * 
 * 
 */
package com.okulapp.chat;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.data.MongoDataAdapter;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.expopush.ExpoPushNotificationUtil;
import com.okulapp.security.SecurityUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class ChatUtil {

    public static List<Map<String, Object>> getClasses(MyDataSBLocal myDataSB, String searcherUserName, String searchText, ObjectId branch) {
        String userRole = SecurityUtil.getUserRoleFromUserTable(myDataSB, searcherUserName);
        Map<String, Object> user = SecurityUtil.getUserFromEmail(myDataSB, searcherUserName);
        QueryBuilder qb = QueryBuilder.start("branch").is(branch);
        if (searchText != null && !searchText.isEmpty()) {
            qb = qb.and("name").is(Pattern.compile(searchText));
        }
        if ("teacher".equals(userRole) && user != null) {//öğretmen sadece kendi sınıflarını listeler
            qb.and("teacher").is(user.get("_id"));
        }

        return myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "classes", qb.get().toMap(), new BasicDBObject());
    }

    public static List<Map<String, Object>> getTeachers(MyDataSBLocal myDataSB, String searcherUserName, String searchText, ObjectId branch) {
        String userRole = SecurityUtil.getUserRoleFromUserTable(myDataSB, searcherUserName);
        QueryBuilder qb = QueryBuilder.start("deleted").exists(false);
        if (branch != null) {
            qb = qb.and("branch").is(branch);
        }
        if (searchText != null && !searchText.isEmpty()) {
            qb = qb.and("nameSurname").is(Pattern.compile(searchText));
        }
        if ("parent".equals(userRole)) {//veli sadece kendi sınıf öğretmenini listeler
            Map<String, Object> user = SecurityUtil.getUserFromEmail(myDataSB, searcherUserName);
            ObjectId classId = (ObjectId) user.get("class");
            if (classId != null) {
                Map<String, Object> cls = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "class", QueryBuilder.start("_id").is(classId).get().toMap());
                if (cls != null) {
                    qb.and("_id").is(cls.get("teacher"));
                }
            }
        }
        return myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "teachers", qb.get().toMap(), QueryBuilder.start("password").is(false).get().toMap());
    }

    public static List<Map<String, Object>> getStudentParentsOfClass(MyDataSBLocal myDataSB, ObjectId clss) {

        QueryBuilder qb = QueryBuilder.start("deleted").exists(false);
        if (clss != null) {
            qb = qb.and("class").is(clss);
        }
        return myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "studentParent", qb.get().toMap(), QueryBuilder.start("password").is(false).get().toMap());
    }

    public static List<Map<String, Object>> getStudentParents(MyDataSBLocal myDataSB, String searcherUserName, String searchText, ObjectId branch) {
        String userRole = SecurityUtil.getUserRoleFromUserTable(myDataSB, searcherUserName);
        if (!Arrays.asList("admin", "teacher").contains(userRole)) {
            return null;
        }
        QueryBuilder qb = QueryBuilder.start("deleted").exists(false);
        if (branch != null) {
            qb = qb.and("branch").is(branch);
        }
        if (searchText != null && !searchText.isEmpty()) {
            qb = qb.and("nameSurname").is(Pattern.compile(searchText));
        }
        if ("teacher".equals(userRole)) {//öğretmen sadece kendi sınıf velilerini listeler
            Map<String, Object> user = SecurityUtil.getUserFromEmail(myDataSB, searcherUserName);
            Map<String, Object> cls = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "class", QueryBuilder.start("teacher").is(user.get("_id")).get().toMap());
            if (cls != null) {
                qb.and("class").is(cls.get("_id"));
            }
        }
        return myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "studentParent", qb.get().toMap(), QueryBuilder.start("password").is(false).get().toMap());
    }

    public static List<Map<String, Object>> getStuffs(MyDataSBLocal myDataSB, String searcherUserName, String searchText, ObjectId branch) {
        String userRole = SecurityUtil.getUserRoleFromUserTable(myDataSB, searcherUserName);
        if (!Arrays.asList("admin", "teacher").contains(userRole)) {
            return null;
        }
        QueryBuilder qb = QueryBuilder.start("deleted").exists(false);
        if (branch != null) {
            qb = qb.and("branch").is(branch);
        }
        if (searchText != null && !searchText.isEmpty()) {
            qb = qb.and("nameSurname").is(Pattern.compile(searchText));
        }
        qb.and(QueryBuilder.start("title").in(Arrays.asList("Okul Müdürü", "Şube Müdürü")).get());
        return myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "stuff", qb.get().toMap(), QueryBuilder.start("password").is(false).get().toMap());
    }

    public static Map<String, Object> startNewConversation(MyDataSBLocal myDataSB, Map<String, Object> receiver, Map<String, Object> user) {
        if (receiver == null || !receiver.containsKey("email")) {
            return null;
        }
        Map<String, Object> query = QueryBuilder
                .start("users").all(Arrays.asList(user.get("email"), receiver.get("email")))
                .and("deleted").is(false).get().toMap();
        Map<String, Object> currentChat = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "chat", query);
        if (currentChat == null) {
            currentChat = new HashMap();
            currentChat.put("_id", new ObjectId());
            currentChat.put("receiverEmail", receiver.get("email"));
            currentChat.put("receiverNameSurname", receiver.get("nameSurname"));
            currentChat.put("senderEmail", user.get("email"));
            currentChat.put("senderNameSurname", user.get("nameSurname"));
            currentChat.put("users", Arrays.asList(user.get("email"), receiver.get("email")));
            currentChat.put("messages", new ArrayList());
            currentChat.put("startDate", new Date());
            currentChat.put("starter", user.get("email"));
            currentChat.put("deleted", false);
            myDataSB.getAdvancedDataAdapter().create(myDataSB.getDbName(), "chat", currentChat);
        }
        List<Map<String, Object>> messages = (List<Map<String, Object>>) currentChat.getOrDefault("messages", new ArrayList());
        for (Map<String, Object> message : messages) {
            if (user.get("email").equals(message.get("receiverEmail"))) {
                message.put("readed", true);
                message.put("readedDateTime", new Date());
            }
        }
        currentChat.put("messages", messages);
        List<String> deletedUsers = (List<String>) currentChat.getOrDefault("deletedUsers", new ArrayList());
        if (deletedUsers.contains(user.get("email").toString())) {
            deletedUsers.remove(user.get("email").toString());
            currentChat.put("deletedUsers", deletedUsers);
        }

        currentChat.put("convReceiverEmail", receiver.get("email"));
        currentChat.put("convReceiverNS", receiver.get("nameSurname"));
        currentChat.put("convReceiverImage", receiver.get("image"));

        myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "chat", currentChat);
        return currentChat;
    }

    public static List<Map<String, Object>> getConversations(MyDataSBLocal myDataSB, String userName, String searchConversation) {
        QueryBuilder qb = QueryBuilder.start("deleted").is(false)
                .and("users").in(Arrays.asList(userName))
                .and("deletedUsers").notIn(Arrays.asList(userName));
        if (searchConversation != null && !searchConversation.trim().isEmpty()) {
            qb.or(QueryBuilder.start("receiverNameSurname").is(Pattern.compile(searchConversation)).get(), QueryBuilder.start("senderNameSurname").is(Pattern.compile(searchConversation)).get());

        }
        List<Map<String, Object>> conversations = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "chat", qb.get().toMap(), new BasicDBObject());
        List<Map<String, Object>> editedList = new ArrayList();
        for (Map<String, Object> conversation : conversations) {
            Map<String, Object> receiver = getConversationReceiver(myDataSB, conversation, userName);
            conversation.put("convReceiverEmail", receiver.get("email"));
            conversation.put("convReceiverNS", receiver.get("nameSurname"));
            conversation.put("convReceiverImage", receiver.get("image"));
            editedList.add(conversation);
        }
        return editedList;
    }

    public static Map<String, Object> getConversationReceiver(MyDataSBLocal myDataSB, Map<String, Object> chat, String userName) {
        if (userName.equals(chat.get("senderEmail"))) {
            return SecurityUtil.getUserFromEmail(myDataSB, chat.get("receiverEmail").toString());
        } else {
            return SecurityUtil.getUserFromEmail(myDataSB, chat.get("senderEmail").toString());
        }
    }

    public static Map<String, Object> getChat(MyDataSBLocal myDataSB, String chatId, String userName) {
        Map<String, Object> chat = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "chat", QueryBuilder.start("_id").is(new ObjectId(chatId)).get().toMap());
        Map<String, Object> receiver = getConversationReceiver(myDataSB, chat, userName);
        chat.put("convReceiverEmail", receiver.get("email"));
        chat.put("convReceiverNS", receiver.get("nameSurname"));
        chat.put("convReceiverImage", receiver.get("image"));
        return chat;
    }

    public static void addMessageToConversation(MyDataSBLocal myDataSB, String currentMessageText, ObjectId currentChatId, String userName, String senderNameSurname) {
        Map<String, Object> chat = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "chat", QueryBuilder.start("_id").is(currentChatId).get().toMap());
        addMessageToConversation(myDataSB, currentMessageText, chat, userName, senderNameSurname);

    }

    public static void addMessageToConversation(MyDataSBLocal myDataSB, String currentMessageText, Map<String, Object> currentChat, String userName, String senderNameSurname) {
        if (currentMessageText == null || currentMessageText.trim().isEmpty() || currentChat == null) {
            return;
        }
        Map<String, Object> message = new HashMap();
        Map<String, Object> receiver = getConversationReceiver(myDataSB, currentChat, userName);
        message.put("message", currentMessageText);
        message.put("sendingTime", new Date());
        message.put("sentStatus", 0);//0 : Not Sended, 1 : Sended, 2: Readed
        message.put("senderEmail", userName);
        message.put("receiverNameSurname", receiver.get("nameSurname"));
        message.put("senderNameSurname", senderNameSurname);
        message.put("receiverEmail", receiver.get("email"));
        message.put("readed", false);
        ArrayList msgList = (ArrayList) currentChat.get("messages");
        msgList.add(message);
        currentChat.put("messages", msgList);
        List<String> deletedUsers = (List<String>) currentChat.getOrDefault("deletedUsers", new ArrayList());
        if (!deletedUsers.isEmpty()) {
            deletedUsers.clear();
            currentChat.put("deletedUsers", deletedUsers);
        }
        myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "chat", currentChat);
        List<String> receivers = new ArrayList();
        receivers.add(receiver.get("email").toString());
        ExpoPushNotificationUtil.sendPushNotificationToQueue(receivers, "Yeni Mesaj", message.get("message").toString());
    }

    public static List<Map<String, Object>> getUnreadedMessages(MyDataSBLocal myDataSB, String userName) {
        MongoDataAdapter adapter = (MongoDataAdapter) myDataSB.getAdvancedDataAdapter().getSelectedDataAdapter();
        List<Document> list = new ArrayList();
        QueryBuilder match = QueryBuilder.start("$match").
                is(QueryBuilder.start("messages.readed").is(false).and("messages.receiverEmail").is(userName).get());
        QueryBuilder project = QueryBuilder.start("$project").
                is(QueryBuilder.start("messages").is(true).get());
        QueryBuilder unwind = QueryBuilder.start("$unwind").
                is("$messages");

        list.add(new Document(match.get().toMap()));
        list.add(new Document(project.get().toMap()));
        list.add(new Document(unwind.get().toMap()));
        list.add(new Document(match.get().toMap()));
        list.add(new Document("$sort", new Document("messages.sendingTime", -1)));
        List<Map<String, Object>> myUnreadedMessages = adapter.executeAggregation(myDataSB.getDbName(), "chat", list);
        return myUnreadedMessages;
    }

    public static List<Map<String, Object>> getUnreadedMessagesInChat(MyDataSBLocal myDataSB, ObjectId chatId, String userName) {
        MongoDataAdapter adapter = (MongoDataAdapter) myDataSB.getAdvancedDataAdapter().getSelectedDataAdapter();
        List<Document> list = new ArrayList();
        QueryBuilder match = QueryBuilder.start("$match").
                is(QueryBuilder.start("messages.readed").is(false).and("messages.receiverEmail").is(userName).and("_id").is(chatId).get());
        QueryBuilder project = QueryBuilder.start("$project").
                is(QueryBuilder.start("messages").is(true).get());
        QueryBuilder unwind = QueryBuilder.start("$unwind").
                is("$messages");

        list.add(new Document(match.get().toMap()));
        list.add(new Document(project.get().toMap()));
        list.add(new Document(unwind.get().toMap()));
        list.add(new Document(match.get().toMap()));
        list.add(new Document("$sort", new Document("messages.sendingTime", -1)));
        List<Map<String, Object>> myUnreadedMessages = adapter.executeAggregation(myDataSB.getDbName(), "chat", list);
        return myUnreadedMessages;
    }
}
