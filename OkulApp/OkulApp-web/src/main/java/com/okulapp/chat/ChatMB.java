/*
 * 
 * 
 * 
 */
package com.okulapp.chat;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.dispatcher.DispatcherMB;
import com.okulapp.forms.CrudMB;
import com.okulapp.security.SecurityMB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "chatMB")
@SessionScoped
public class ChatMB implements Serializable {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private boolean showRecents;
    private boolean showContacts;
    private boolean showChat;
    private boolean canSeeStudentParent;
    private String searchConversation;
    private String searchContact;
    private String currentMessageText;
    private List<Map<String, Object>> conversations;
    private List<Map<String, Object>> branches;
    private ObjectId selectedBranch;
    private List<Map<String, Object>> listStuff;
    private List<Map<String, Object>> listTeacher;
    private List<Map<String, Object>> listStudentParent;
    private Map<String, Object> currentChat;

    @EJB
    MyDataSBLocal myDataSB;

    @Inject
    CrudMB crudMB;

    private @Inject
    SecurityMB securityMB;

    private @Inject
    DispatcherMB dispatcherMB;

    /**
     * Creates a new instance of ChatMB
     */
    public ChatMB() {
    }

    public List<Map<String, Object>> getBranches() {
        return branches;
    }

    public List<Map<String, Object>> getConversations() {
        return conversations;
    }

    public Map<String, Object> getCurrentChat() {
        return currentChat;
    }

    public String getCurrentMessageText() {
        return currentMessageText;
    }

    public List<Map<String, Object>> getListStuff() {
        return listStuff;
    }

    public List<Map<String, Object>> getListTeacher() {
        return listTeacher;
    }

    public List<Map<String, Object>> getListStudentParent() {
        return listStudentParent;
    }

    public String getSearchContact() {
        return searchContact;
    }

    public String getSearchConversation() {
        return searchConversation;
    }

    public ObjectId getSelectedBranch() {
        return selectedBranch;
    }

    @PostConstruct
    public void init() {
        showRecents = true;
        showContacts = false;
        showChat = false;
        canSeeStudentParent = Arrays.asList("admin", "teacher").contains(securityMB.getLoginUserRole());
        branches = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "branch", new BasicDBObject(), new BasicDBObject());
        if (branches != null) {
            setSelectedBranch((ObjectId) branches.get(0).get("_id"));
        }
        searchConversationsAction();
        refreshChat();
    }

    public void searchContactsAction() {
        QueryBuilder qb = QueryBuilder.start();
        if (selectedBranch != null) {
            qb = qb.and("branch").is(selectedBranch);
        }
        if (searchContact != null && !searchContact.isEmpty()) {
            qb = qb.and("nameSurname").is(Pattern.compile(searchContact));
        }

        listTeacher = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "teachers", qb.get().toMap(), new BasicDBObject());
        listStudentParent = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "studentParent", qb.get().toMap(), new BasicDBObject());
        listStuff = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "stuff", qb.and(QueryBuilder.start("title").in(Arrays.asList("Okul Müdürü", "Şube Müdürü")).get()).get().toMap(), new BasicDBObject());
    }

    public void searchConversationsAction() {
        QueryBuilder qb = QueryBuilder.start("deleted").is(false)
                .and("users").in(Arrays.asList(securityMB.getRemoteUserName()))
                .and("deletedUsers").notIn(Arrays.asList(securityMB.getRemoteUserName()));
        if (searchConversation != null && !searchConversation.trim().isEmpty()) {
            qb.and("users").is(Pattern.compile(searchConversation));
        }
        conversations = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "chat", qb.get().toMap(), new BasicDBObject());
    }

    public void setConversations(List<Map<String, Object>> conversations) {
        this.conversations = conversations;
    }

    public void setCurrentChat(Map<String, Object> currentChat) {
        this.currentChat = currentChat;
    }

    public void setCurrentMessageText(String currentMessageText) {
        this.currentMessageText = currentMessageText;
    }

    public void setListStuff(List<Map<String, Object>> listStuff) {
        this.listStuff = listStuff;
    }

    public void setListTeacher(List<Map<String, Object>> listTeacher) {
        this.listTeacher = listTeacher;
    }

    public void setListStudentParent(List<Map<String, Object>> listStudentParent) {
        this.listStudentParent = listStudentParent;
    }

    public void setSearchContact(String searchContact) {
        this.searchContact = searchContact;
    }

    public void setSearchConversation(String searchConversation) {
        this.searchConversation = searchConversation;
    }

    public void showRecentsForm() {
        showRecents = true;
        showContacts = false;
        showChat = false;
        searchConversationsAction();
    }

    public void showContactsForm() {
        showRecents = false;
        showContacts = true;
        showChat = false;
    }

    public void showChatForm() {
        showRecents = false;
        showContacts = false;
        showChat = true;
    }

    public boolean isShowRecents() {
        return showRecents;
    }

    public void setBranches(List<Map<String, Object>> branches) {
        this.branches = branches;
    }

    public void setSelectedBranch(ObjectId selectedBranch) {
        this.searchContactsAction();
        this.selectedBranch = selectedBranch;
    }

    public void setShowRecents(boolean showRecents) {
        this.showRecents = showRecents;
    }

    public boolean isShowContacts() {
        return showContacts;
    }

    public void setShowContacts(boolean showContacts) {
        this.showContacts = showContacts;
    }

    public boolean isShowChat() {
        return showChat;
    }

    public void setShowChat(boolean showChat) {
        this.showChat = showChat;
    }

    public StreamedContent handleImageFileDownload(ObjectId fileId) {
        return crudMB.handleImageFileDownload(fileId);
    }

    public void startNewConversation(Map<String, Object> receiver) {
        if (receiver == null || !receiver.containsKey("email")) {
            return;
        }
        currentChat = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "chat", QueryBuilder.start("users").is(securityMB.getRemoteUserName()).and("users").is(receiver.get("email")).and("deleted").is(false).get().toMap());
        if (currentChat == null) {
            currentChat = new HashMap();
            currentChat.put("_id", new ObjectId());
            currentChat.put("receiverEmail", receiver.get("email"));
            currentChat.put("receiverNameSurname", receiver.get("nameSurname"));
            currentChat.put("senderEmail", securityMB.getRemoteUserName());
            currentChat.put("senderNameSurname", securityMB.getLoginUser().get("nameSurname"));
            currentChat.put("users", Arrays.asList(securityMB.getRemoteUserName(), receiver.get("email")));
            currentChat.put("messages", new ArrayList());
            currentChat.put("startDate", new Date());
            currentChat.put("starter", securityMB.getRemoteUserName());
            currentChat.put("deleted", false);
            myDataSB.getAdvancedDataAdapter().create(myDataSB.getDbName(), "chat", currentChat);
        }
        List<String> deletedUsers = (List<String>) currentChat.getOrDefault("deletedUsers", new ArrayList());
        if (deletedUsers.contains(securityMB.getRemoteUserName())) {
            deletedUsers.remove(securityMB.getRemoteUserName());
            currentChat.put("deletedUsers", deletedUsers);
            myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "chat", currentChat);
        }
        showChatForm();
    }

    public boolean isCanSeeStudentParent() {
        return canSeeStudentParent;
    }

    public void addMessageToConversation() {
        if (currentMessageText == null || currentMessageText.trim().isEmpty() || currentChat == null) {
            return;
        }
        Map<String, Object> message = new HashMap();
        message.put("message", currentMessageText);
        message.put("sendingTime", new Date());
        message.put("sentStatus", 0);//0 : Not Sended, 1 : Sended, 2: Readed
        message.put("senderEmail", securityMB.getRemoteUserName());
        ArrayList msgList = (ArrayList) currentChat.get("messages");
        msgList.add(message);
        currentChat.put("messages", msgList);
        List<String> deletedUsers = (List<String>) currentChat.getOrDefault("deletedUsers", new ArrayList());
        if (!deletedUsers.isEmpty()) {
            deletedUsers.clear();
            currentChat.put("deletedUsers", deletedUsers);
        }
        currentMessageText = "";
        myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "chat", currentChat);
    }

    public String formatDateTime(Date dt) {
        return dt == null ? "" : sdf.format(dt);
    }

    public void deleteMessageFromMe() {
        if (currentChat != null) {
            List<String> users = (List<String>) currentChat.getOrDefault("users", new ArrayList());
            List<String> deletedUsers = (List<String>) currentChat.getOrDefault("deletedUsers", new ArrayList());
            if (!deletedUsers.contains(securityMB.getRemoteUserName())) {
                deletedUsers.add(securityMB.getRemoteUserName());
            }
            if (deletedUsers.size() == users.size()) {
                currentChat.put("deleted", true);
            }
            currentChat.put("deletedUsers", deletedUsers);
            myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "chat", currentChat);
            searchConversationsAction();
            showRecentsForm();
        }
    }

    public Map<String, Object> getConversationReceiver(Map<String, Object> chat) {
        if (securityMB.getRemoteUserName().equals(chat.get("senderEmail"))) {
            return securityMB.getUserFromEmail(chat.get("receiverEmail").toString());
        } else {
            return securityMB.getUserFromEmail(chat.get("senderEmail").toString());
        }
    }

    public void switchToChat(Map<String, Object> chat) {
        setCurrentChat(chat);
        showChatForm();
    }

    public void refreshChat() {
        if (currentChat != null) {
            currentChat = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "chat", QueryBuilder.start("_id").is(currentChat.get("_id")).get().toMap());
        }
    }
}
