/*
 * 
 * 
 * 
 */
package com.okulapp.ws;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "webSocketSessionManager")
@ApplicationScoped
public class WebSocketSessionManager {

    private List<Session> socketSessions = new ArrayList<Session>();

    /**
     * Creates a new instance of WebSocketSessionManager
     */
    public WebSocketSessionManager() {
    }

    public List<Session> getSocketSessions() {
        return socketSessions;
    }

    public void setSocketSessions(List<Session> socketSessions) {
        this.socketSessions = socketSessions;
    }

}
