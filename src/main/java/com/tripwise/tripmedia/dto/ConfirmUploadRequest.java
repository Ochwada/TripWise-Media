package com.tripwise.tripmedia.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.dto
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Tuesday,  26.Aug.2025 | 10:36
 * Description :
 * ================================================================
 */
@Data
public class ConfirmUploadRequest {
    @NotBlank
    private String mediaId;

    @NotBlank
    private String checksum;

    @Positive
    private long bytes;

    private Integer width;

    private Integer height;
}
