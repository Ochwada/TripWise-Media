package com.tripwise.tripmedia.dto;

import com.tripwise.tripmedia.model.Media;
import com.tripwise.tripmedia.model.MediaStatus;
import com.tripwise.tripmedia.model.MediaVariant;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.dto
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 18:00
 * Description :DTO returned by the API for a media item. Contains basic metadata, status, variants, and timestamps.
 * - Intended for read-only responses.
 * ================================================================
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponse {
    /**
     * Media document ID.
     */
    private String id;

    /**
     * Owner user ID.
     */
    private String userId;

    /**
     * Journal the media belongs to.
     */
    private String journalId;

    /**
     * Original filename.
     */
    private String filename;

    /**
     * MIME type (e.g., image/jpeg).
     */
    private String mimeType;

    /**
     * Optional checksum (e.g., MD5/SHA256).
     */
    private String checksum;

    /**
     * Storage key (e.g., S3 object key).
     */
    private String storageKey;

    /**
     * Public CDN URL.
     */
    private String cdnUrl;

    /**
     * Size in bytes.
     */
    private Long bytes;

    /**
     * Pixel width (if applicable).
     */
    private Integer width;

    /**
     * Pixel height (if applicable).
     */
    private Integer height;

    /**
     * Free-form tags.
     */
    private List<String> tags;

    /**
     * Generated/transcoded variants.
     */
    private List<MediaVariant> variants;

    /**
     * Processing/publication status.
     */
    private MediaStatus status;


    /**
     * Creation timestamp (UTC).
     */
    private Instant createdAt;

    /**
     * Last update timestamp (UTC).
     */
    private Instant updatedAt;

    /**
     * Build a {@code MediaResponse} from a {@code Media} entity.
     * Maps fields by name;
     */
    public static MediaResponse from(Media m) {

        if (m == null) return null;

        return MediaResponse.builder()
                .id(m.getId())
                .userId(m.getUserId())
                .journalId(m.getJournalId())
                .filename(m.getFileName())
                .mimeType(m.getMimeType())
                .bytes(m.getBytes())
                .tags(m.getTags() != null ? m.getTags() : new ArrayList<>())
                .variants(m.getVariants() != null ? m.getVariants() : new ArrayList<>())
                .status(m.getStatus())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }

}
