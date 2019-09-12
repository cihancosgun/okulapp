/*
 * 
 * 
 * 
 */
package com.okulapp.ws;

import com.mongodb.BasicDBObject;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.Session;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@ServerEndpoint("/ws")
public class WebSocketServer {

    @Inject
    WebSocketSessionManager sessionManager;

    @OnMessage
    public void onMessage(String message, final Session session) {
        Logger.getLogger(WebSocketServer.class.getName()).info("Message from " + session.getId() + ": " + message);
        BasicDBObject dboMsg = BasicDBObject.parse(message);
        if (dboMsg != null) {
            List<String> receivers = (List<String>) dboMsg.get("receivers");
            if (receivers != null) {
                for (Session socketSession : sessionManager.getSocketSessions()) {
                    if (socketSession.getId() != session.getId() && receivers.contains(socketSession.getUserPrincipal().getName())) {
                        socketSession.getAsyncRemote().sendText(dboMsg.toJson());
                    }
                }

            }
        }
    }

    @OnOpen
    public void open(Session session) {
        if (session.getUserPrincipal() == null) {
            try {
                session.close();
                return;
            } catch (IOException ex) {
                Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        Logger.getLogger(WebSocketServer.class.getName()).info("connected " + session.getId());
        sessionManager.getSocketSessions().add(session);
    }

    @OnClose
    public void close(Session session) {
        Logger.getLogger(WebSocketServer.class.getName()).info("disconnected " + session.getId());
        sessionManager.getSocketSessions().remove(session);
    }

}
