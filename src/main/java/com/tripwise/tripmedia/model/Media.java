package com.tripwise.tripmedia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.model
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 16:55
 * Description : Represents a media in a document in MongoDB
 * ================================================================
 */
@Document(collation = "media")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Media {
    /**
     * Unique identifier of the media document in MongoDB.
     */
    @Id
    private String id;

    /**
     * Identifier of the journal or entity this media belongs to.
     */
    private String journalId;

    /**
     * Original file name of the uploaded media.
     */
    private String fileName;

    /**
     * MIME type of the file (e.g., {@code image/png}, {@code video/mp4}).
     */
    private String mimeType;

    /**
     * File size in bytes.
     */
    private Long bytes;

    /**
     * Tags for categorizing or searching the media.
     * - Example: {@code ["cover", "thumbnail", "profile"]}.
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    /**
     * Available variants of this media (e.g., thumbnail, medium, original).
     */
    @Builder.Default
    private List<MediaVariant> variants = new ArrayList<>();

    /**
     * Current processing status of the media (e.g., UPLOADING, READY, FAILED, DELETED).
     */
    @Builder.Default
    private MediaStatus status = MediaStatus.UPLOADING;

    /**
     * Timestamp when the media was created.
     */
    private Instant createdAt;

    /**
     * Timestamp when the media was last updated.
     */
    private Instant updatedAt;

}
