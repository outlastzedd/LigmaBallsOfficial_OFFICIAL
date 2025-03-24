package service;

import gmail.IMailJava;
import gmail.MailJava;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService implements IMailJava {

    @Override
    public boolean send(String to, String subject, String messageContent) {
        try {
            // Set mail properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", MailJava.HOST_NAME);
            props.put("mail.smtp.socketFactory.port", MailJava.SSL_PORT);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.port", MailJava.SSL_PORT);

            // Create Authenticator
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MailJava.APP_EMAIL, MailJava.APP_PASSWORD);
                }
            };

            // Create a session with the authenticator
            Session session = Session.getInstance(props, auth);

            // Create a message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailJava.APP_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, "UTF-8");
            message.setContent(messageContent, "text/html; charset=UTF-8");

            // Send the message
            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}