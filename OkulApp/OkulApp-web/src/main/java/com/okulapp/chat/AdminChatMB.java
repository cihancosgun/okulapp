/*
 * 
 * 
 * 
 */
package com.okulapp.chat;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.data.okul.MyDataSBLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "adminChatMB")
@SessionScoped
public class AdminChatMB implements Serializable {

    @EJB
    MyDataSBLocal myDataSB;

    private CrudListResult chatList = null;
    private CrudListResult filteredChatList = null;
    private String globalFilter = "";
    private boolean showDetail = false;
    private Map<String, Object> currentChat;

    /**
     * Creates a new instance of AdminChatMB
     */
    public AdminChatMB() {
    }

    public CrudListResult getChatList() {
        return chatList;
    }

    public Map<String, Object> getCurrentChat() {
        return currentChat;
    }

    public CrudListResult getFilteredChatList() {
        return filteredChatList;
    }

    public String getGlobalFilter() {
        return globalFilter;
    }

    @PostConstruct
    public void init() {
        chatList = myDataSB.getAdvancedDataAdapter().getSortedList(myDataSB.getDbName(), "chat", new BasicDBObject("deleted", false), new BasicDBObject("messages", false), new BasicDBObject("startDate", -1));
    }

    public void filterListener(AjaxBehaviorEvent filterEvent) {
        if (globalFilter != null) {
            chatList = myDataSB.getAdvancedDataAdapter().getSortedList(myDataSB.getDbName(), "chat", new BasicDBObject("deleted", false).append("users", Pattern.compile(globalFilter)), new BasicDBObject("messages", false), new BasicDBObject("startDate", -1));
        }
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setChatList(CrudListResult chatList) {
        this.chatList = chatList;
    }

    public void setCurrentChat(Map<String, Object> currentChat) {
        this.currentChat = currentChat;
    }

    public void setFilteredChatList(CrudListResult filteredChatList) {
        this.filteredChatList = filteredChatList;
    }

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public void swtichToChat(Map<String, Object> rec) {
        setCurrentChat(myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "chat", QueryBuilder.start("_id").is(rec.get("_id")).get().toMap()));
        setShowDetail(true);
    }
}
