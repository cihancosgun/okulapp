/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.security;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.okulapp.forms.CrudForm;
import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
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

    private List<Byte> userRoles = null;
    private final Map<String, Byte> RolesToByte = new HashMap<>();
    private final Map<Byte, String> ByteToRole = new HashMap<>();

    public SecurityMB() {
    }

    @PostConstruct
    public void init() {
        RolesToByte.put("admin", (byte) 0);
        RolesToByte.put("teacher", (byte) 1);
        RolesToByte.put("stuff", (byte) 2);
        RolesToByte.put("parent", (byte) 3);

        ByteToRole.put((byte) 0, "admin");
        ByteToRole.put((byte) 1, "teacher");
        ByteToRole.put((byte) 2, "stuff");
        ByteToRole.put((byte) 3, "parent");
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

    public void signOut() {
        try {
            getRequest().logout();
            getRequest().getSession().invalidate();
            getResponse().sendRedirect("/OkulApp-web/");
        } catch (ServletException ex) {
            Logger.getLogger(SecurityMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecurityMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Byte> getUserRoles() {
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
                            byte oldRoleOrder = userRoles.isEmpty() ? 100 : userRoles.get(0);
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

    public Byte[] getUserRolesArray() {
        Byte[] result = new Byte[getUserRoles().size()];
        getUserRoles().toArray(result);
        return result;
    }

    public void setUserRoles(List<Byte> userRoles) {
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
}