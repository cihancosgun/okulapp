/*
 * 
 * 
 * 
 */
package com.okulapp;

import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.expopush.ExpoPushNotificationUtil;
import com.okulapp.mail.MailSender;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/bilgiyuvamQueue")
})
public class OkulAppMDB implements MessageListener {

    @Inject
    MyDataSBLocal myDataSB;

    @Resource
    private MessageDrivenContext mdc;

    public OkulAppMDB() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof MapMessage) {
                MapMessage mapMsg = (MapMessage) message;
                if ("sendMail".equals(mapMsg.getString("messageType"))) {
                    MailSender.send_mailToSMTP(mapMsg.getString("mail"), mapMsg.getString("subject"), mapMsg.getString("body"));
                } else if ("sendPushNotification".equals(mapMsg.getString("messageType"))) {
                    String receiverEmailstr = mapMsg.getString("receiverEmails");
                    List<String> receiverEmails = Arrays.asList(receiverEmailstr.replace("[", "").replace("]", "").replaceAll(" ", "").split(","));
                    ExpoPushNotificationUtil.sendPushNotificationToServer(myDataSB, receiverEmails, mapMsg.getString("title"), mapMsg.getString("body"));
                }
            } else {
                System.out.println("wrong message type " + message.getClass().getName());

            }
        } catch (JMSException e) {
//            e.printStackTrace();
            mdc.setRollbackOnly();
        } catch (Throwable te) {
//            te.printStackTrace();
        }

    }

}
