package org.library.util;

import org.library.init.InitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    private static Connection connection;
    private static final String usernameKey = "database.username";
    private static final String passwordKey = "database.password";
    private static final String urlKey = "database.url";

    private Util() {}

    public static synchronized Connection getConnection() {
        if(connection != null) {
            return connection;
        }
        try {
            String urlValue;
            String passwordValue;
            String usernameValue;

            if (InitProperties.getProperty(urlKey).isPresent()
                    && InitProperties.getProperty(passwordKey).isPresent()
                    && InitProperties.getProperty(usernameKey).isPresent()) {

                passwordValue = InitProperties.getProperty(passwordKey).get();
                urlValue = InitProperties.getProperty(urlKey).get();
                usernameValue = InitProperties.getProperty(usernameKey).get();
            } else {
                logger.warn("Проверьте данные для подключения к базе данных и попробуйте снова.");
                throw new IllegalArgumentException("Ошибка подключения.");
            }

            logger.info("Попытка соеденения с базой данных - {}", urlValue);
            connection = DriverManager.getConnection(urlValue,usernameValue,passwordValue);

        } catch (SQLException ex) {
            logger.warn("Проверьте данные для подключения к базе данных и попробуйте снова.");
            logger.warn(ex.getMessage());
            throw new IllegalArgumentException("Ошибка подключения.");
        }
        logger.info("Соеденение с базой данных успешно установлено.");
        return connection;
    }
}
