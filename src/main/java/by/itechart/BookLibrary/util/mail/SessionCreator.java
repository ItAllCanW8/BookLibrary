package by.itechart.BookLibrary.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SessionCreator {
    private static final String EMAIL = "mail.user.name";
    private static final String PASSWORD = "mail.user.password";

    public static Session createSession(Properties configProperties) {
        String userName = configProperties.getProperty(EMAIL);
        String userPassword = configProperties.getProperty(PASSWORD);
        return Session.getDefaultInstance(configProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, userPassword);
            }
        });
    }
}
