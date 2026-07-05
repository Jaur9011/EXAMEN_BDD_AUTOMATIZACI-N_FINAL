package com.juice.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.juice.log.LogManager;

/**
 * Carga y expone las propiedades definidas en config.properties.
 * Se carga una sola vez desde el classpath (funciona tanto en IDE como en Maven).
 */
public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ConfigReader.class);

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("No se encontro config.properties en el classpath");
            }
            properties.load(is);
            log.info("config.properties cargado correctamente");
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new RuntimeException("La propiedad '" + key + "' no existe en config.properties");
        }
        return value;
    }

    public static String getBrowser() {
        return get("browser").toLowerCase();
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static String getAppUrl() {
        return get("app.url");
    }

    public static int getImplicitTimeout() {
        return Integer.parseInt(get("timeout.implicit"));
    }

    public static int getExplicitTimeout() {
        return Integer.parseInt(get("timeout.explicit"));
    }

    public static String getScreenshotPath() {
        return get("screenshot.path");
    }
}
