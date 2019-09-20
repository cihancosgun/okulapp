/*
 * 
 * 
 * 
 */
package com.okulapp.notify;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.dispatcher.DispatcherMB;
import com.okulapp.forms.CrudMB;
import com.okulapp.security.SecurityMB;
import com.okulapp.utils.ByteArrayUploadedFile;
import com.okulapp.ws.WSReceiverManager;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "notificationMB")
@SessionScoped
public class NotificationMB implements Serializable {

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

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private List<Map<String, Object>> branches;
    private ObjectId selectedBranch;
    private String searchContact;
    private List<Map<String, Object>> listClasses;
    private List<Map<String, Object>> listStuff;
    private List<Map<String, Object>> listTeacher;
    private List<Map<String, Object>> listStudentParent;
    private TreeNode rootContacts;
    private TreeNode[] selectedContacts;

    private String notificationMessage;
    private List<UploadedFile> uploadedFiles = new ArrayList<>();
    private List<UploadedFile> uploadedThumbFiles = new ArrayList<>();
    private BasicDBObject currentNotify = new BasicDBObject();

    /**
     * Creates a new instance of NotificationMB
     */
    public NotificationMB() {
    }

    public List<Map<String, Object>> getBranches() {
        return branches;
    }

    public Map<String, Object> getCurrentNotify() {
        return currentNotify;
    }

    public List<Map<String, Object>> getListClasses() {
        return listClasses;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public TreeNode getRootContacts() {
        return rootContacts;
    }

    public String getSearchContact() {
        return searchContact;
    }

    public ObjectId getSelectedBranch() {
        return selectedBranch;
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

    public TreeNode[] getSelectedContacts() {
        return selectedContacts;
    }

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public List<UploadedFile> getUploadedThumbFiles() {
        return uploadedThumbFiles;
    }

    @PostConstruct
    public void init() {
        branches = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "branch", new BasicDBObject(), new BasicDBObject());
        if (branches != null && securityMB.getLoginUser() != null) {
            setSelectedBranch((ObjectId) securityMB.getLoginUser().getOrDefault("branch", branches.get(0).get("_id")));
        }
    }

    public HttpServletRequest getRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request;
    }

    public void searchContactsAction() {
        QueryBuilder qb = QueryBuilder.start();
        if (selectedBranch != null) {
            qb = qb.and("branch").is(selectedBranch);
        }
        if (searchContact != null && !searchContact.isEmpty()) {
            qb = qb.and("nameSurname").is(Pattern.compile(searchContact));
        }

        listClasses = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "classes", QueryBuilder.start("branch").is(selectedBranch).get().toMap(), new BasicDBObject());
        listTeacher = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "teachers", qb.get().toMap(), new BasicDBObject());
        listStudentParent = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "studentParent", qb.get().toMap(), new BasicDBObject());
        listStuff = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "stuff", qb.and(QueryBuilder.start("title").in(Arrays.asList("Okul Müdürü", "Şube Müdürü")).get()).get().toMap(), new BasicDBObject());

        createContactsTree();
    }

    public void createContactsTree() {
        rootContacts = new CheckboxTreeNode(new Document("nameSurname", "Kişiler"), null);

        TreeNode stuffs = new CheckboxTreeNode(new Document("nameSurname", "Personel"), rootContacts);
        TreeNode teachers = new CheckboxTreeNode(new Document("nameSurname", "Öğretmen"), rootContacts);
        TreeNode studentParents = new CheckboxTreeNode(new Document("nameSurname", "Veli"), rootContacts);

        for (Map<String, Object> rec : listStuff) {
            CheckboxTreeNode dummy = new CheckboxTreeNode("Personel", rec, stuffs);
        }

        for (Map<String, Object> recT : listTeacher) {
            CheckboxTreeNode dummyC = new CheckboxTreeNode("Öğretmen", recT, teachers);
        }

        for (Map<String, Object> rec : listClasses) {
            rec.put("nameSurname", rec.get("name"));
            CheckboxTreeNode dummyB = new CheckboxTreeNode("Veli", rec, studentParents);
            for (Map<String, Object> recSP : listStudentParent) {
                if (recSP.get("class").equals(rec.get("_id"))) {
                    CheckboxTreeNode dummyC = new CheckboxTreeNode("Veli", recSP, dummyB);
                }
            }
        }
    }

    public CheckboxTreeNode prepearNodeForPersonRecord(Map<String, Object> rec) {
        return new CheckboxTreeNode("", rec, null);
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesMessage msg = new FacesMessage("Başarılı", event.getFile().getFileName() + " isimli dosya yüklendi.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            BufferedImage imageOrj = ImageIO.read(event.getFile().getInputstream());
            BufferedImage imageThumb = myDataSB.getFileUpDownManager().resizeImage(imageOrj, 200, 200);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(imageThumb, "png", bos);
            UploadedFile resizedImage = new ByteArrayUploadedFile(bos.toByteArray(), event.getFile().getFileName(), event.getFile().getContentType());
            uploadedFiles.add(event.getFile());
            uploadedThumbFiles.add(resizedImage);
        } catch (IOException ex) {
            Logger.getLogger(NotificationMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setBranches(List<Map<String, Object>> branches) {
        this.branches = branches;
    }

    public void setCurrentNotify(BasicDBObject currentNotify) {
        this.currentNotify = currentNotify;
    }

    public void setListClasses(List<Map<String, Object>> listClasses) {
        this.listClasses = listClasses;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public void setRootContacts(TreeNode rootContacts) {
        this.rootContacts = rootContacts;
    }

    public void setSearchContact(String searchContact) {
        this.searchContact = searchContact;
    }

    public void setSelectedBranch(ObjectId selectedBranch) {
        this.selectedBranch = selectedBranch;
        this.searchContactsAction();
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

    public void setSelectedContacts(TreeNode[] selectedContacts) {
        this.selectedContacts = selectedContacts;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public void clean() {
        notificationMessage = "";
        uploadedFiles.clear();
    }

    public List<String> getReceiverUsers() {
        List<String> list = new ArrayList();
        if (selectedContacts != null) {
            for (TreeNode rec : selectedContacts) {
                if (rec.getData() instanceof Document && ((Document) rec.getData()).containsKey("email")) {
                    list.add(((Document) rec.getData()).getString("email"));
                }
            }
        }
        return list;
    }

    private List<String> getReceiverUsersNS() {
        List<String> list = new ArrayList();
        if (selectedContacts != null) {
            for (TreeNode rec : selectedContacts) {
                if (rec.getData() instanceof Document && ((Document) rec.getData()).containsKey("email")) {
                    list.add(((Document) rec.getData()).getString("nameSurname"));
                }
            }
        }
        return list;
    }

    private List<ObjectId> getFileIds() {
        List<ObjectId> list = new ArrayList();
        for (UploadedFile uploadedFile : uploadedFiles) {
            try {
                ObjectId fileId = myDataSB.getFileUpDownManager().uploadFile(uploadedFile.getInputstream(), uploadedFile.getFileName());
                if (fileId != null) {
                    list.add(fileId);
                }
            } catch (IOException ex) {
                Logger.getLogger(NotificationMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    private List<ObjectId> getThumbFileIds() {
        List<ObjectId> list = new ArrayList();
        for (UploadedFile uploadedFile : uploadedThumbFiles) {
            try {
                ObjectId fileId = myDataSB.getFileUpDownManager().uploadFile(uploadedFile.getInputstream(), uploadedFile.getFileName());
                if (fileId != null) {
                    list.add(fileId);
                }
            } catch (IOException ex) {
                Logger.getLogger(NotificationMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    public BasicDBObject prepareNotifiyRecord(String messageType) {
        BasicDBObject rec = new BasicDBObject();
        rec.put("_id", new ObjectId());
        rec.put("senderEmail", securityMB.getRemoteUserName());
        rec.put("senderNameSurname", securityMB.getLoginUser().get("nameSurname"));
        rec.put("users", getReceiverUsers());
        rec.put("usersNS", getReceiverUsersNS());
        rec.put("readedUsers", new ArrayList());
        rec.put("messageType", messageType);
        rec.put("message", notificationMessage);
        rec.put("fileIds", getFileIds());
        rec.put("thumbFileIds", getThumbFileIds());
        rec.put("startDate", new Date());
        rec.put("deleted", false);
        return rec;
    }

    public StreamedContent handleMessageTypeIcon(String messageType) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(messageType.concat(".png"));
        if (is != null) {
            return new DefaultStreamedContent(is, "image/png");
        } else {
            return new DefaultStreamedContent(getClass().getClassLoader().getResourceAsStream("board.png"), "image/png");
        }
    }

    public void sendMessage(String messageType) {
        if (notificationMessage == null || notificationMessage.trim().isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Lütfen bir mesaj giriniz.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        if (selectedContacts == null || selectedContacts.length == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Lütfen en az bir alıcı seçiniz.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        currentNotify = prepareNotifiyRecord(messageType);

        myDataSB.getAdvancedDataAdapter().create(myDataSB.getDbName(), "notifications", currentNotify);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Mesajınız iletildi.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        receiverManager.setReceivers(getReceiverUsers());
        clean();
    }

    public void setUploadedThumbFiles(List<UploadedFile> uploadedThumbFiles) {
        this.uploadedThumbFiles = uploadedThumbFiles;
    }
}
