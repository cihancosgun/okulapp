/*
 * 
 * 
 * 
 */
package com.okulapp.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class MailSender {

    public static void send_mailToQueue(String mail, String subject, String body) throws UnsupportedEncodingException {
        Context ctx;
        try {
            ctx = new InitialContext();

            ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("jms/bilgiyuvamQueueConnectionFactory");
            Queue queue = (Queue) ctx.lookup("jms/bilgiyuvamQueue");

            try (javax.jms.Connection connection = connectionFactory.createConnection()) {
                try (javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE)) {
                    try (MessageProducer messageProducer = session.createProducer(queue)) {
                        MapMessage message = session.createMapMessage();
                        message.setString("messageType", "sendMail");
                        message.setString("mail", mail);
                        message.setString("subject", subject);
                        message.setString("body", body);
                        messageProducer.send(message);
                    }
                }

            } catch (JMSException ex) {
                Logger.getLogger("mail").log(Level.SEVERE, "sendmail", ex);
            }

        } catch (NamingException ex) {
            Logger.getLogger("mail").log(Level.SEVERE, "sendmail", ex);
        }
    }

    public static void send_mailToSMTP(String mail, String subject, String body) throws UnsupportedEncodingException {
        boolean debug = true;
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.bilgiyuvamanaokulu.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", debug);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", false);
        props.put("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("app@bilgiyuvamanaokulu.com", "fk?rq~yrqfZE");
            }
        });
        session.setDebug(debug);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("app@bilgiyuvamanaokulu.com", "Bilgiyuvam Anaokulu"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject(subject);
            try {
                message.setContent(body, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (Exception ex) {
                Logger.getLogger("mail").log(Level.SEVERE, "sendmail", ex);
            }
        } catch (MessagingException e) {

            throw new RuntimeException(e);

        }
    }
}
