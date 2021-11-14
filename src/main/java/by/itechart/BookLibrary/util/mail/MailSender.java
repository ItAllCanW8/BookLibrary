package by.itechart.BookLibrary.util.mail;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class MailSender {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String MAIL_PROPERTY_FILE_PATH = "/mail.properties";
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final Properties properties;

    private MimeMessage message;
    private String recipientEmail;
    private String letterSubject;
    private String letterContent;

    private MailSender() {
    }

    static {
        properties = new Properties();
        try {
            properties.load(MailSender.class.getResourceAsStream(MAIL_PROPERTY_FILE_PATH));
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Error loading mail properties", e );
        }
    }

    private static class Holder {
        static final MailSender INSTANCE = new MailSender();
    }

    public static MailSender getInstance() {
        return MailSender.Holder.INSTANCE;
    }

    public void setupLetter(String recipientEmail, String letterSubject, String letterContent) {
        this.recipientEmail = recipientEmail;
        this.letterSubject = letterSubject;
        this.letterContent = letterContent;
    }

    public void send() {
        try {
            initMessage();
            Transport.send(message);
        } catch (AddressException e) {
            LOGGER.log(Level.ERROR, "Invalid email: " + recipientEmail + e);
        } catch (MessagingException e) {
            LOGGER.log(Level.ERROR, "Error generating or sending message: " + e);
        }
    }

    private void initMessage() throws MessagingException {
        Session mailSession = SessionCreator.createSession(properties);

        message = new MimeMessage(mailSession);
        message.setSubject(letterSubject);
        message.setContent(letterContent, CONTENT_TYPE);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
    }
}
