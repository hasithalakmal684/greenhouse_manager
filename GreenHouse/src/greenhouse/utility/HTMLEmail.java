/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Hasitha Lakmal
 */
public class HTMLEmail {

    private final InputStream input;
    private final Properties prop;

    public HTMLEmail() throws FileNotFoundException, IOException {
        input = new FileInputStream("./resources/email.properties");
        prop = new Properties();
        prop.load(input);
    }

    public void sendAccountVerificationEmail(String to, String altPass) {
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("email_username"), prop.getProperty("email_password"));
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("email_username")));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject("Greeenhouse Account Verification!");
            message.setContent("<h4>Use following credentials to log into the system for first time</h4><br>"
                    + "<h3>Email : " + to + "</h3>"
                    + "<h3>Alternative password : " + altPass + "</h3>"
                    + "<h5>Note : Do not use these after verfifcation.</h5>", "text/html");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public void sendPasswordResetEmail(String to, String verificationCode) {
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("email_username"), prop.getProperty("email_password"));
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("email_username")));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject("Greeenhouse Password Reset!");
            message.setContent("<h3>"+verificationCode+"</h3><h4> is your verification code for password reset.</h4><br>", "text/html");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
