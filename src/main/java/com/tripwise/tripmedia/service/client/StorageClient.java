package com.tripwise.tripmedia.service.client;

import java.net.URL;
import java.util.*;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.service.client
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 18:17
 * Description : Abstraction over an object storage service (e.g., S3)
 * - Provides presigned upload URLs, object deletion, and (optional) public URLs.
 * -Implementations should be thread-safe.
 * ================================================================
 */
public interface StorageClient {

    /**
     * Immutable payload for a presigned HTTP PUT upload.
     *
     * @param storageKey canonical storage key/object name
     * @param url        time-limited URL to perform the PUT request
     * @param headers    request headers required by the provider (never {@code null}, may be empty)
     */
    record PresignedPut(String storageKey, URL url, Map<String, String> headers) {
    }

    /**
     * Create a time-limited URL for uploading an object via HTTP PUT.
     *
     * @param objectKey     canonical storage key/object name within the bucket/container
     * @param contentType   MIME type of the object to upload (e.g., {@code image/jpeg})
     * @param contentLength size in bytes; some providers require this for signing
     * @return a {@link PresignedPut} containing the URL and any required headers
     * @throws IllegalArgumentException if inputs are invalid (e.g., negative {@code contentLength})
     */
    PresignedPut presignPut(String objectKey, String contentType, long contentLength);

    /**
     * Delete the object at the given key.
     * - Should be idempotent: deleting a non-existent object is a no-op.</p>
     *
     * @param objectKey storage key/object name
     */
    void deleteObject(String objectKey);

    /**
     * Return a public, cacheable URL for reading the object, if available.
     * - Implementations may return {@code null} if the object has no public endpoint.
     *
     * @param objectKey storage key/object name
     * @return a stable public URL, or {@code null} if not publicly accessible
     */
    String publicUrl(String objectKey);
}
