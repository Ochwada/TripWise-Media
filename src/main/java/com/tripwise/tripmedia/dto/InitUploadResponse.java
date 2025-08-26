package com.tripwise.tripmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Map;

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
 *
 *
 *  Response of {@code initUpload}: presigned URL and required headers to PUT the file.
 *  Integrity/dimension metadata is supplied later in {@code confirmUpload}.
 *
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


    /** Presigned PUT URL (expires). */
    @NotBlank
    private String uploadUrl;

    /** Any headers the client must include when uploading to the presigned URL. */
    @Builder.Default
    private Map<String, String> headers = Map.of();
}

