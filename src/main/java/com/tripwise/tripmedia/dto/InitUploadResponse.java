package com.tripwise.tripmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.dto
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 17:53
 * Description : Response DTO returned after initializing a media upload.
 * ================================================================
 */

/**
 *  {
 *    "mediaId": "64f3b0d1e8a9cd7a12345abc",
 *    "storageKey": "media/uploads/img123.png",
 *    "checksum": "e99a18c428cb38d5f260853678922e03",
 *    "bytes": 204800,
 *    "width": 1024,
 *    "height": 768
 *  }
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitUploadResponse {
    /**
     * Unique identifier of the created media entry in the database.
     */
    private String mediaId;

    /**
     * Storage key (e.g., S3 object key or path in the storage backend)
     * where the uploaded file will be stored.
     */
    @NotBlank
    private String storageKey;

    /**
     * File checksum (e.g., MD5, SHA-256) used to verify file integrity
     * after upload.
     */
    @NotBlank
    private String checksum;

    /**
     * File size in bytes.
     * Must be a positive value.
     */
    @Positive
    private long bytes;

    /**
     * Width of the media in pixels (if applicable, e.g., for images/videos).
     * -May be {@code null} if the media type does not have dimensions.
     */
    private Integer width;

    /**
     * Height of the media in pixels (if applicable, e.g., for images/videos).
     * -May be {@code null} if the media type does not have dimensions.
     */
    private Integer height;
}

