package com.tripwise.tripmedia.service.client;

import java.net.URL;
import java.util.Map;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.service.client
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Tuesday,  26.Aug.2025 | 10:44
 * Description :
 * ================================================================
 */
public record PresignedPut(String storageKey, URL url, Map<String, String> headers) {
    // public record PresignedPut(String storageKey, URL url, Map<String, String> headers) {}

}
