package com.tripwise.tripmedia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.config
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 16:27
 * Description : Configuration class that enables auditing support for MongoDB
 * ================================================================
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
    /**
     * {@code @EnableMongoAuditing} annotation enables Spring Data to automatically populate fields annotated with {@code
     * @CreatedDate} and {@code @LastModifiedDate} during entity persistence.
     * *
     * This class does not require any methods or fields; its presence in the application context is sufficient for
     * activating auditing behavior.
     * */
}
