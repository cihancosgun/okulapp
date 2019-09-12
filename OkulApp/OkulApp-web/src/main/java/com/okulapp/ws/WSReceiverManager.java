/*
 * 
 * 
 * 
 */
package com.okulapp.ws;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "wSReceiverManager")
@SessionScoped
public class WSReceiverManager implements Serializable {

    private List<String> receivers;

    /**
     * Creates a new instance of WSReceiverManager
     */
    public WSReceiverManager() {
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

}
