package com.tripwise.tripmedia.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.service.client
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Tuesday,  26.Aug.2025 | 10:20
 * Description :
 * ================================================================
 */
@Component
@RequiredArgsConstructor
public class JournalClient {
    private final WebClient web;

    /** Adjust the path to real endpoint; throws if not found/not owned. */
    public void assertOwnership(String journalId, String userId ) {
        web.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/journals/{id}")
                                .queryParam("userId", userId)
                        .build(journalId))
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofSeconds(4));
    }


}
