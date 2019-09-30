/*
 * 
 * 
 * 
 */
package com.okulapp.expopush;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.crud.dao.CrudListResult;
import com.okulapp.data.okul.MyDataSBLocal;
import com.sun.jersey.api.client.WebResource;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

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
                        message.setObject("receiverEmails", receiverEmails.toString());
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
        CrudListResult list = myData.getAdvancedDataAdapter().getList(myData.getDbName(), "users", QueryBuilder.start("login").in(receiverEmails).and("pushToken").exists(true).get().toMap(), QueryBuilder.start("pushToken").is(true).get().toMap());
        for (Map<String, Object> rec : list) {
            pushNotificationIds.add(rec.get("pushToken").toString());
        }
        if (!pushNotificationIds.isEmpty()) {
            sendToExpoServer(pushNotificationIds, title, body);
        }
    }

    static Client getJerseyHTTPSClient() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = getSslContext();
        HostnameVerifier allHostsValid = new NoOpHostnameVerifier();

        return ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier(allHostsValid)
                .build();
    }

    private static SSLContext getSslContext() throws NoSuchAlgorithmException,
            KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1");

        KeyManager[] keyManagers = null;
        TrustManager[] trustManager = {new NoOpTrustManager()};
        SecureRandom secureRandom = new SecureRandom();

        sslContext.init(keyManagers, trustManager, secureRandom);

        return sslContext;
    }

    private static Client getClient() {
        if (myClient == null) {
            try {
                myClient = getJerseyHTTPSClient();
            } catch (KeyManagementException ex) {
                Logger.getLogger(ExpoPushNotificationUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ExpoPushNotificationUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return myClient;
    }

    private static void sendToExpoServer(List<String> receivers, String title, String body) {
        List<String> datas = new ArrayList();
        for (String receiver : receivers) {
            datas.add(new BasicDBObject("to", receiver).append("title", title).append("body", body).toJson());
        }
        String data = datas.toString();
        try {
            String serviceUrl = "https://exp.host/--/api/v2/push";
            Response response = getClient().target(serviceUrl).path("send").request().post(Entity.json(data));
            response.toString();
        } catch (Exception e) {
            Logger.getLogger("sendtoexposerver").log(Level.SEVERE, "send", e);
        }

    }
}
