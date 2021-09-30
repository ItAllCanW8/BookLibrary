package by.itechart.BookLibrary.model.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Test {
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";
    private static final String DB_DRIVER = "db.driver";

    private static String userr;

    public void init(){

    }
    public Connection getConnection(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String url = properties.getProperty(DB_URL);
            String user = properties.getProperty(DB_USER);
            userr = user;
            String password = properties.getProperty(DB_PASSWORD);

            Class.forName(properties.getProperty(DB_DRIVER));

            return DriverManager.getConnection(url, user, password);
        } catch (IOException | ClassNotFoundException | SQLException e ){
            throw new RuntimeException();
        }
    }
}
