package com.tripwise.tripmedia;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TripmediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripmediaApplication.class, args);
    }

    static {
        // Load environment variables from .env file
        // Ignores file if missing (useful for production environments like Heroku)
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // List of expected keys to load from the .env file
        String[] envVars = {
                "SPRING_PROFILES_ACTIVE",
                "SPRING_APPLICATION_NAME",
                "SPRING_DATA_MONGODB_URI",
                "GOOGLE_CLIENT_ID",
                "MEDIA_BUCKET",
                "MEDIA_PUBLIC_BASE_URL",
                "MEDIA_S3_ENDPOINT",
                "MEDIA_S3_REGION",
                "MEDIA_S3_ACCESS_KEY",
                "MEDIA_S3_SECRET_KEY",
                "MEDIA_S3_PATH_STYLE_ACCESS"
        };
        // Iterate through keys and set them as JVM system properties if found

        for (String key : envVars) {

            String value = dotenv.get(key);

            if (value != null) {
                System.setProperty(key, value); // Makes it accessible via System.getProperty
                System.out.println("✅ " + key + " loaded and set.");
            }else {
                System.out.println("⚠️" + key + " not found in env variables. Skipping System property.");
            }
        }

    }

}
