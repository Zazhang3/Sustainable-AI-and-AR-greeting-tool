package com.tool.greeting_tool.server;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Send e-mail
 * @noinspection CallToPrintStackTrace
 */
public class MailSender {

    /**
     * Send mail
     * @param username :mail sender
     * @param password :mail sender password
     * @param recipient :mail Recipient
     * @param subject :e-mail subject
     * @param body :tokens
     */
    public static void sendEmail(final String username, final String password, String recipient, String subject, String body) {
        //new thread to avoid block main thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //set Mail server properties
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.office365.com"); //SMTP server
                    props.put("mail.smtp.port", "587"); //SMTP port

                    // Session to authenticate the host and get the session
                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

                    // Compose the message
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username)); // Sender's email address
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient)); // Recipient's email address
                    message.setSubject(subject); // Email subject
                    message.setText(body); // Email body


                    // Send message
                    Transport.send(message);
                    System.out.println("Email sent successfully");

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
