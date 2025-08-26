package com.tripwise.tripmedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.config
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Tuesday,  26.Aug.2025 | 10:58
 * Description :
 * ================================================================
 */
@Configuration
public class JournalWebClientConfig {

    @Bean
    public WebClient journalWebClient(WebClient.Builder builder,
                                      @Value("${journals.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
