/*
 * 
 * 
 * 
 */
package com.okulapp.expopush;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.data.okul.MyDataSBLocal;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class ExpoPushNotificationUtil {

    private static WebResource myWebResource;
    private static Client myClient;

    public static void sendPushNotificationToQueue(List<String> receiverEmails, String title, String body) {
        Context ctx;
        try {
            ctx = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("jms/bilgiyuvamQueueConnectionFactory");
            Queue queue = (Queue) ctx.lookup("jms/bilgiyuvamQueue");
            try (javax.jms.Connection connection = connectionFactory.createConnection()) {
                try (javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE)) {
                    try (MessageProducer messageProducer = session.createProducer(queue)) {
                        MapMessage message = session.createMapMessage();
                        message.setString("messageType", "sendPushNotification");
                        message.setObject("receiverEmails", receiverEmails);
                        message.setString("title", title);
                        message.setString("body", body);
                        messageProducer.send(message);
                    }
                }
            } catch (JMSException ex) {
                Logger.getLogger("sendPushNotification").log(Level.SEVERE, "sendPushNotification", ex);
            }

        } catch (NamingException ex) {
            Logger.getLogger("sendPushNotification").log(Level.SEVERE, "sendPushNotification", ex);
        }
    }

    public static void sendPushNotificationToServer(MyDataSBLocal myData, List<String> receiverEmails, String title, String body) {
        List<String> pushNotificationIds = new ArrayList();
        CrudListResult list = myData.getAdvancedDataAdapter().getList(myData.getDbName(), "users", QueryBuilder.start("login").in(receiverEmails).get().toMap(), QueryBuilder.start("pushToken").is(true).get().toMap());
        for (Map<String, Object> rec : list) {
            pushNotificationIds.add(rec.get("pushToken").toString());
        }
        if (!pushNotificationIds.isEmpty()) {
            sendToExpoServer(pushNotificationIds, title, body);
        }
    }

    private static WebResource getWebResource() {
        String serviceUrl = "https://exp.host/--/api/v2/push/send";
        if (myClient == null) {
            myClient = Client.create();
        }
        if (myWebResource == null) {
            myWebResource = myClient.resource(serviceUrl);
        }
        return myWebResource;
    }

    private static void sendToExpoServer(List<String> receivers, String title, String body) {
        List<String> datas = new ArrayList();
        for (String receiver : receivers) {
            datas.add(new BasicDBObject("to", receiver).append("title", title).append("body", body).toJson());
        }
        String data = new Gson().toJson(datas);
        ClientResponse clientResponse = getWebResource().path("create").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, data);
    }
}
