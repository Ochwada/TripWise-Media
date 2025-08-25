package com.tripwise.tripmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.dto
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 17:47
 * Description : Request DTO for initializing a media upload process.
 * ================================================================
 */

 /**
 * Request DTO for initializing a media upload process.
  * *
 * This object is typically sent by the client to the server before starting an upload. It provides metadata about the file
 * to be uploaded so that the server can prepare storage and validation.
  * {
  *    "fileName": "profile.png",
  *    "mimeType": "image/png",
  *    "bytes": 204800,
  *    "journalId": "journal-123"
  *  * }
 */
@Data
public class InitUploadRequest {
     /**
      * The original file name of the media (e.g., "image.png").
      */
    @NotBlank
    private String fileName;

     /**
      * The MIME type of the file (e.g., "image/png", "video/mp4").
      */
    @NotBlank
    private String mimeType;

     /**
      * The size of the file in bytes.
      * - Must be a positive number.
      */
    @Positive
    private long bytes;

     /**
      * Optional ID of the journal or entity this media is associated with.
      */
    private String journalId;
}
