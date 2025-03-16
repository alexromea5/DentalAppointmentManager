package ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = Settings.class.getClassLoader()
                .getResourceAsStream("settings.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Nu s-a gasit fisierul settings.properties");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Eroare la incarcarea fisierului de setari: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
