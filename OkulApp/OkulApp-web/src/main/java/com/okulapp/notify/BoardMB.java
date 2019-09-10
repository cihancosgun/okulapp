/*
 * 
 * 
 * 
 */
package com.okulapp.notify;

import com.mongodb.QueryBuilder;
import com.okulapp.chat.ChatMB;
import com.okulapp.data.MongoDataAdapter;
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
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "boardMB")
@SessionScoped
public class BoardMB implements Serializable {

    @EJB
    MyDataSBLocal myDataSB;

    @Inject
    CrudMB crudMB;

    @Inject
    ChatMB chatMB;

    private @Inject
    SecurityMB securityMB;

    private @Inject
    DispatcherMB dispatcherMB;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private List<Map<String, Object>> myBoard = new ArrayList();
    private List<Map<String, Object>> myUnreadedBoard = new ArrayList();
    private Date lastMessageDate = new Date();
    private ObjectId currentImage;
    private Map<String, Object> currentNotify;

    /**
     * Creates a new instance of BoardMB
     */
    public BoardMB() {
    }

    public ObjectId getCurrentImage() {
        return currentImage;
    }

    public Map<String, Object> getCurrentNotify() {
        return currentNotify;
    }

    public List<Map<String, Object>> getMyBoard() {
        return myBoard;
    }

    public List<Map<String, Object>> getMyUnreadedBoard() {
        return myUnreadedBoard;
    }

    @PostConstruct
    public void init() {
        refreshMyBoard();
    }

    public void refreshMyBoard() {
        MongoDataAdapter adapter = (MongoDataAdapter) myDataSB.getAdvancedDataAdapter().getSelectedDataAdapter();
        QueryBuilder filter = QueryBuilder.start("deleted").is(false)
                .and("users").in(Arrays.asList(securityMB.getRemoteUserName()));
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
        filter.and("readedUsers").notIn(Arrays.asList(securityMB.getRemoteUserName()));
        myUnreadedBoard = adapter.getSortedPagedList(myDataSB.getDbName(), "notifications", filter.get().toMap(), projection.get().toMap(), 0, 20, QueryBuilder.start("startDate").is(-1).get().toMap());
        if (!myUnreadedBoard.isEmpty()) {
            Date dtMsgDate = (Date) myUnreadedBoard.get(0).get("startDate");
            if (dtMsgDate.after(lastMessageDate)) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bildirim", String.format("%d adet okunmamış bildiriminiz var.", myUnreadedBoard.size()));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            lastMessageDate = dtMsgDate;
        }
    }

    public void setCurrentImage(ObjectId currentImage) {
        this.currentImage = currentImage;
    }

    public void setCurrentNotify(Map<String, Object> currentNotify) {
        List<String> readedUsers = (List<String>) currentNotify.get("readedUsers");
        if (!readedUsers.contains(securityMB.getRemoteUserName())) {
            readedUsers.add(securityMB.getRemoteUserName());
        }
        currentNotify.put("readedUsers", readedUsers);
        myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "notifications", currentNotify);
        this.currentNotify = currentNotify;
    }

    public void setMyBoard(List<Map<String, Object>> myBoard) {
        this.myBoard = myBoard;
    }

    public void setMyUnreadedBoard(List<Map<String, Object>> myUnreadedBoard) {
        this.myUnreadedBoard = myUnreadedBoard;
    }

    public void refreshAllNotify() {
        if (!dispatcherMB.getCurrentPage().getPageUrl().contains("home.xhtml")) {
            refreshMyBoard();
        }
        if (!dispatcherMB.getCurrentPage().getPageUrl().contains("chat.xhtml")) {
            chatMB.refreshMyUnreadMessages();
        }
    }
}
