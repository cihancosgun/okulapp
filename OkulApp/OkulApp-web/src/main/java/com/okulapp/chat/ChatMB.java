/*
 * 
 * 
 * 
 */
package com.okulapp.chat;

import com.mongodb.BasicDBObject;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.dispatcher.DispatcherMB;
import com.okulapp.forms.CrudMB;
import com.okulapp.security.SecurityMB;
import com.okulapp.ws.WSReceiverManager;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
    private List<Map<String, Object>> myUnreadedMessages;
    private Date lastMessageDate = new Date();

    @EJB
    MyDataSBLocal myDataSB;

    @Inject
    CrudMB crudMB;

    private @Inject
    SecurityMB securityMB;

    private @Inject
    DispatcherMB dispatcherMB;

    private @Inject
    WSReceiverManager receiverManager;

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
        securityMB.getUserRoles();
        canSeeStudentParent = Arrays.asList("admin", "teacher").contains(securityMB.getLoginUserRole());
        branches = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "branch", new BasicDBObject(), new BasicDBObject());
        if (branches != null && securityMB.getLoginUser() != null) {
            setSelectedBranch((ObjectId) securityMB.getLoginUser().getOrDefault("branch", branches.get(0).get("_id")));
        }
        searchConversationsAction();
        refreshChat();
        refreshMyUnreadMessages();
    }

    public void searchContactsAction() {
        listTeacher = ChatUtil.getTeachers(myDataSB, securityMB.getRemoteUserName(), searchContact, selectedBranch);
        listStudentParent = ChatUtil.getStudentParents(myDataSB, securityMB.getRemoteUserName(), searchContact, selectedBranch);
        listStuff = ChatUtil.getStuffs(myDataSB, securityMB.getRemoteUserName(), searchContact, selectedBranch);
    }

    public void searchConversationsAction() {
        conversations = ChatUtil.getConversations(myDataSB, securityMB.getRemoteUserName(), searchConversation);
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
        this.selectedBranch = selectedBranch;
        this.searchContactsAction();
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

    public HttpServletRequest getRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request;
    }

    public void startNewConversation(Map<String, Object> receiver) {
        currentChat = ChatUtil.startNewConversation(myDataSB, receiver, securityMB.getLoginUser());
        receiverManager.setReceivers((List<String>) currentChat.get("users"));
        showChatForm();
    }

    public boolean isCanSeeStudentParent() {
        return canSeeStudentParent;
    }

    public void addMessageToConversation() {
        ChatUtil.addMessageToConversation(myDataSB, currentMessageText, currentChat, securityMB.getRemoteUserName(), securityMB.getLoginUser().get("nameSurname").toString());
        currentMessageText = "";
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

    public List<Map<String, Object>> getMyUnreadedMessages() {
        return myUnreadedMessages;
    }

    public void refreshMyUnreadMessages() {
        myUnreadedMessages = ChatUtil.getUnreadedMessages(myDataSB, securityMB.getRemoteUserName());
        if (!myUnreadedMessages.isEmpty()) {
            Date dtMsgDate = (Date) ((Map) myUnreadedMessages.get(0).get("messages")).get("sendingTime");
            if (dtMsgDate.after(lastMessageDate)) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mesaj", String.format("%d adet okunmamış mesajınız var.", myUnreadedMessages.size()));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            lastMessageDate = dtMsgDate;
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
        startNewConversation(getConversationReceiver(chat));
    }

    public void refreshChat() {
        if (currentChat != null) {
            currentChat = ChatUtil.getChat(myDataSB, ((ObjectId) currentChat.get("_id")).toHexString(), securityMB.getRemoteUserName());
        }
    }
}
