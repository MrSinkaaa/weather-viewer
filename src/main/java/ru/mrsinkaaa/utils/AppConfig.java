package ru.mrsinkaaa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static Properties properties = new Properties();

    static {
        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if(inputStream == null) {
                throw new RuntimeException("Could not load application.properties");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
