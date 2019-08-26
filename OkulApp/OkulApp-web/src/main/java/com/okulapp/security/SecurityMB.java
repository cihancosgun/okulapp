/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.security;

import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        RolesToByte.put("uysarchitect", (byte) 0);
        RolesToByte.put("uysadmin", (byte) 1);
        RolesToByte.put("kpbadmin", (byte) 2);
        RolesToByte.put("uysuser", (byte) 3);
        RolesToByte.put("pyuser", (byte) 4);
        RolesToByte.put("spkuser", (byte) 5);
        RolesToByte.put("takasbank", (byte) 6);
        RolesToByte.put("tuik", (byte) 7);
        RolesToByte.put("tcmb", (byte) 8);
        RolesToByte.put("anonim", (byte) 9);

        ByteToRole.put((byte) 0, "uysarchitect");
        ByteToRole.put((byte) 1, "uysadmin");
        ByteToRole.put((byte) 2, "kpbadmin");
        ByteToRole.put((byte) 3, "uysuser");
        ByteToRole.put((byte) 4, "pyuser");
        ByteToRole.put((byte) 5, "spkuser");
        ByteToRole.put((byte) 6, "takasbank");
        ByteToRole.put((byte) 7, "tuik");
        ByteToRole.put((byte) 8, "tcmb");
        ByteToRole.put((byte) 9, "anonim");
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

}
