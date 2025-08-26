package com.tripwise.tripmedia.service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.service.client
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 18:21
 * Description : S3-based implementation of the {@link StorageClient} interface.
 * - This class provides methods for interacting with an S3-compatible storage service,
 * * including generating presigned upload URLs, deleting objects, and generating public URLs.
 * - It uses both {@link S3Client} for direct S3 API calls and {@link S3Presigner} for generating temporary upload links.
 * ================================================================
 */

/**
 * <pre>
 * media:
 *   bucket: my-media-bucket
 *   public-base-url: https://cdn.example.com/media
 */
@Component
@Profile("s3")
public class S3StorageClient implements StorageClient {

    private final S3Client s3;
    private final S3Presigner presigner;
    private final String bucket;
    private final String publicBaseUrl;

    /**
     * Constructs a new {@code S3StorageClient}.
     *
     * @param s3            the low-level {@link S3Client} for direct S3 API operations
     * @param presigner     the {@link S3Presigner} for generating presigned URLs
     * @param bucket        the S3 bucket name where media files are stored
     * @param publicBaseUrl the optional public base URL for accessing files via CDN;
     *                      if blank or null, public URLs are disabled
     */
    public S3StorageClient(
            S3Client s3,
            S3Presigner presigner,
            @Value("${media.bucket}") String bucket,
            @Value("${media.public-base-url:}") String publicBaseUrl) {
        this.s3 = s3;
        this.presigner = presigner;
        this.bucket = bucket;
        this.publicBaseUrl = (
                publicBaseUrl == null ||
                        publicBaseUrl.isBlank())
                ? null : publicBaseUrl.replaceAll("/$", "");
    }

    /**
     * Generates a presigned PUT URL that allows a client to upload a file directly to S3.
     * <p>
     * The presigned URL is valid for 20 minutes.
     *
     * @param key         the object key (path) inside the S3 bucket
     * @param contentType the MIME type of the file to upload
     * @param bytes       the size of the file in bytes (not used in presign but useful for validation)
     * @return a {@link PresignedPut} object containing the presigned URL and metadata
     */
    @Override
    public PresignedPut presignPut(String key, String contentType, long bytes) {
        var put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(bytes)
                .build();

        var pre = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(20))
                .putObjectRequest(put)
                .build();

        URL url = presigner.presignPutObject(pre).url();

        // Tell the client which headers they MUST send with the PUT.
        // If you signed them, they are required by S3 to match exactly.
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", contentType);

        return new PresignedPut(key, url, headers);

    }

    /**
     * Deletes an object from the S3 bucket.
     *
     * @param key the object key (path) of the file to delete
     */
    @Override
    public void deleteObject(String key) {
        s3.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build());
    }

    /**
     * Returns a public URL for the given object key.
     * If {@code publicBaseUrl} is not configured, this method returns {@code null}.
     *
     * @param key the object key (path) inside the S3 bucket
     * @return the public URL to access the file, or {@code null} if no public base URL is set
     */
    @Override
    public String publicUrl(String key) {
        return publicBaseUrl == null ? null : publicBaseUrl + "/" + key;
    }



}
