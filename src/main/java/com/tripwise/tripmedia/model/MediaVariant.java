package com.tripwise.tripmedia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.model
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 17:03
 * Description : Represents a variant of a media file (e.g., image, video, or document) that may differ by resolution,
 * size, or storage location.
 * ================================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaVariant {
    /**
     * The type of variant (e.g., THUMBNAIL, MEDIUM, ORIGINAL).
     */
    private VariantType variantType;

    /**
     * The width of the media in pixels (if applicable).
     */
    private Integer width;

    /**
     * The height of the media in pixels (if applicable).
     */
    private Integer height;

    /**
     * The file size of the media variant in bytes.
     */
    private long bytes;

    /**
     * The storage key (e.g., S3 object key or local storage path)
     * where this media variant is stored.
     */
    private String storageKey;

    /**
     * The public CDN (Content Delivery Network) URL for accessing this media variant.
     */
    private String cdnUrl;

}
