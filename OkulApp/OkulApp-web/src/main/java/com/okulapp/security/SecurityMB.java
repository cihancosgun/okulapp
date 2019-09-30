/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.security;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.forms.CrudForm;
import com.okulapp.mail.MailSender;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;

/**
 *
 * @author cihan
 */
@Named(value = "securityMB")
@SessionScoped
public class SecurityMB implements Serializable {

    @EJB
    MyDataSBLocal myDataSB;

    private List<Integer> userRoles = null;
    private final Map<String, Integer> RolesToByte = new HashMap<>();
    private final Map<Integer, String> ByteToRole = new HashMap<>();
    private String loginUserRole;
    private Map<String, Object> loginUser;
    private Map<String, Object> userCache = new HashMap<>();

    public SecurityMB() {
    }

    @PostConstruct
    public void init() {
        RolesToByte.put("admin", 0);
        RolesToByte.put("teacher", 1);
        RolesToByte.put("stuff", 2);
        RolesToByte.put("parent", 3);

        ByteToRole.put(0, "admin");
        ByteToRole.put(1, "teacher");
        ByteToRole.put(2, "stuff");
        ByteToRole.put(3, "parent");
    }

    public HttpServletRequest getRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request;
    }

    public HttpServletResponse getResponse() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        return response;
    }

    public String getRemoteUserName() {
        return getRequest().getRemoteUser();
    }

    public void setLoginUserRole(String loginUserRole) {
        this.loginUserRole = loginUserRole;
    }

    public void signOut() {
        try {
            getRequest().logout();
            getRequest().getSession().invalidate();
        } catch (ServletException ex) {
            Logger.getLogger(SecurityMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Integer> getUserRoles() {
        if (userRoles == null) {
            userRoles = new ArrayList<>();
            Subject subject;
            try {
                int i = 0;
                subject = (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
                for (Principal principal : subject.getPrincipals()) {
                    if (!principal.getName().contains("=")) {
                        if (i > 0) {
                            String role = principal.getName();
                            setLoginUserRole(role);
                            Integer oldRoleOrder = userRoles.isEmpty() ? 100 : userRoles.get(0);
                            if (RolesToByte.containsKey(role)) {
                                if (RolesToByte.get(role) < oldRoleOrder) {
                                    userRoles.add(0, RolesToByte.get(role));
                                } else {
                                    userRoles.add(RolesToByte.get(role));
                                }
                            }
                        }
                    }
                    i++;
                }

            } catch (PolicyContextException ex) {
                java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return userRoles;
    }

    public Integer[] getUserRolesArray() {
        Integer[] result = new Integer[getUserRoles().size()];
        getUserRoles().toArray(result);
        return result;
    }

    public void setUserRoles(List<Integer> userRoles) {
        this.userRoles = userRoles;
    }

    public void syncUser(Map<String, Object> userRecord, CrudForm cf) {
        if (userRecord.containsKey("email")) {
            Map<String, Object> dboUser = cf.getAda().read(cf.getDbName(), "users", new BasicDBObject("login", userRecord.get("email").toString()));
            if (dboUser == null) {
                createUser(cf, userRecord.get("email").toString(), userRecord.get("password").toString().toCharArray(), getUserRoleFromCrudForm(cf, userRecord));
            } else {
                changePassword(cf, (ObjectId) dboUser.get("_id"), userRecord.get("password").toString().toCharArray());
            }
        }
    }

    private String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    private char[] concatenatePasswordWithSalt(char[] password, char[] salt) {
        char[] passwordWithSalt = new char[password.length + salt.length];
        System.arraycopy(password, 0, passwordWithSalt, 0, password.length);
        System.arraycopy(salt, 0, passwordWithSalt, password.length, salt.length);
        return passwordWithSalt;
    }

    private ObjectId createUser(CrudForm cf, String login, char[] password, String group) {
        String randomSalt = generateRandomSalt();
        Vector<String> groups = new Vector<String>();
        groups.add(group);
        DBObject newUser = BasicDBObjectBuilder.start()
                .append("login", login)
                .append("password", PasswordHasher.hash(concatenatePasswordWithSalt(password, randomSalt.toCharArray()), "SHA-512"))
                .append("salt", randomSalt)
                .append("groups", groups)
                .get();
        cf.getAda().create(cf.getDbName(), "users", newUser.toMap());
        try {
            String strPassword = new String(password);
            MailSender.send_mailToQueue(login, "Bilgiyuvam E-Okul Uygulaması Kullanıcı Bilgileriniz", "Kullanıcı Adınız : " + login + "\r\nParolanız : " + strPassword);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SecurityMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (ObjectId) newUser.get("_id");
    }

    public void changePassword(CrudForm cf, ObjectId userId, char[] newPassword) {
        SecureRandom random = new SecureRandom();
        String salt = new BigInteger(130, random).toString(32);
        DBObject update = BasicDBObjectBuilder.start()
                .push("$set")
                .append("password", PasswordHasher.hash(concatenatePasswordWithSalt(newPassword, salt.toCharArray()), "SHA-512"))
                .append("salt", salt)
                .pop()
                .get();
        cf.getAda().update(cf.getDbName(), "users", update.toMap());
    }

    public void deleteUser(Map<String, Object> userRecord, CrudForm cf) {
        if (userRecord.containsKey("email")) {
            Map<String, Object> rec = cf.getAda().read(cf.getDbName(), "users", new BasicDBObject("login", userRecord.get("email").toString()).toMap());
            if (rec != null) {
                cf.getAda().delete(cf.getDbName(), "users", rec);
            }
        }
    }

    private String getUserRoleFromCrudForm(CrudForm cf, Map<String, Object> userRecord) {
        if ("stuff".equals(cf.getCrudFormCode())) {
            if (Arrays.asList("Okul Müdürü", "Şube Müdürü").contains(userRecord.get("title").toString())) {
                return "admin";
            } else {
                return "stuff";
            }
        } else if ("teachers".equals(cf.getCrudFormCode())) {
            return "teacher";
        } else if ("stuff".equals(cf.getCrudFormCode())) {
            return "stuff";
        } else if ("studentParent".equals(cf.getCrudFormCode())) {
            return "parent";
        } else {
            return "stuff";
        }
    }

    public String getUserRoleFromUserTable(String email) {
        return SecurityUtil.getUserRoleFromUserTable(myDataSB, email);
    }

    public Map<String, Object> getUserFromEmail(String email) {
        Map<String, Object> user = null;
        if (email != null) {
            user = (Map<String, Object>) userCache.get(email);
            if (user == null) {
                user = SecurityUtil.getUserFromEmail(myDataSB, email);
                userCache.put(email, user);
            }
        }
        return user;
    }

    public Map<String, Object> getLoginUser() {
        if (loginUser == null && getRemoteUserName() != null) {
            return getUserFromEmail(getRemoteUserName());
        }
        return loginUser;
    }

    public String getLoginUserRole() {
        return loginUserRole;
    }

}
