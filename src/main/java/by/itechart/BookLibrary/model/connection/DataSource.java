package by.itechart.BookLibrary.model.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
    private static final Logger LOGGER = LogManager.getLogger();
    private static BasicDataSource ds = new BasicDataSource();

    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_MIN_IDLE = "db.minIdle";
    private static final String DB_MAX_IDLE = "db.maxIdle";

    static {
        try (InputStream input = DataSource.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            byte minIdle = Byte.parseByte(properties.getProperty(DB_MIN_IDLE));
            byte maxIdle = Byte.parseByte(properties.getProperty(DB_MAX_IDLE));
            String url = properties.getProperty(DB_URL);
            String user = properties.getProperty(DB_USER);
            String password = properties.getProperty(DB_PASSWORD);

            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(password);
            ds.setMinIdle(minIdle);
            ds.setMaxIdle(maxIdle);

            LOGGER.info("Connection pool initialized.");
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Error loading db properties.", e);
            throw new RuntimeException("Error loading db properties.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DataSource(){ }
}