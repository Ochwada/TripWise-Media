package com.tripwise.tripmedia.service.client;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

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
