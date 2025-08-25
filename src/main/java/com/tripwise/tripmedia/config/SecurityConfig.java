package com.tripwise.tripmedia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.config
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 16:34
 * Description : Configures Spring Security to protect all endpoints using OAuth2 with JWT tokens.
 * ================================================================
 */

@Configuration
public class SecurityConfig {
    /**
     *  This configuration sets up a security filter chain that:
     *  - Permits unauthenticated access to API documentation endpoints such as
     *  *      " /swagger-ui/**" and "/v3/api-docs/**".
     *  - Requires authentication for all other requests.
     *  - Configures the application as an OAuth2 Resource Server using JWT-based authentication.
     * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .anyRequest()
                        .authenticated()
                ).oauth2ResourceServer(o ->o
                        .jwt(jwtConfigurer -> {
                            // Default JWT decoder is based on spring.security.oauth2.resourceserver.jwt.issuer-uri
                            // You can add a custom converter/decoder here if needed
                        })
                );
        // Finalise and return the filter chain
        return http.build(); // returns SecurityFilterChain object and registers it with Spring.
    };
}
