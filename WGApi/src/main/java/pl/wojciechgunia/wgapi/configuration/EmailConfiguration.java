package pl.wojciechgunia.wgapi.configuration;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EmailConfiguration {
    private final String email;
    private final String password;
    private Authenticator auth;
    private Session session;
    private Properties properties;

    public EmailConfiguration(@Value("${notification.mail}") String email, @Value("${notification.password}") String password) {
        this.email = email;
        this.password = password;
        config();
    }

    private void config() {
        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587;

        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        this.auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        };
    }

    private void refreshSession() {
        session = Session.getInstance(properties,auth);
    }

    public void sendMail(String recipientEmail, String content,String subject,boolean onCreate) {
        if (session == null) {
            refreshSession();
        }
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            if (onCreate) {
                refreshSession();
                sendMail(recipientEmail,content,subject,false);
            }
        }
    }
}