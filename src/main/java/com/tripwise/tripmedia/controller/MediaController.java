package com.tripwise.tripmedia.controller;


import com.tripwise.tripmedia.dto.ConfirmUploadRequest;
import com.tripwise.tripmedia.dto.InitUploadRequest;
import com.tripwise.tripmedia.dto.InitUploadResponse;
import com.tripwise.tripmedia.dto.MediaResponse;
import com.tripwise.tripmedia.service.MediaService;
import com.tripwise.tripmedia.service.client.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.controller
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Tuesday,  26.Aug.2025 | 10:17
 * Description :
 * ================================================================
 */
@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService service;

    /**
     * Initializes a new media upload for the authenticated user.
     * This endpoint delegates to {@link MediaService#initUpload(String, InitUploadRequest)}
     * to:
     * - Validate that the user owns the target journal.
     * - Create a new media entry with status {@code UPLOADING}.
     * - Generate a presigned upload URL and required headers via the {@link StorageClient}.
     * <p>
     * The authenticated user's ID is extracted from the {@link Jwt} principal. By default, {@link Jwt#getSubject()}
     * is used, but this can be adjusted if the user identifier resides in another claim.
     *
     * @param jwt     the authenticated user principal (JWT token)
     * @param request the upload initialization request containing journal ID,
     *                file name, MIME type, and file size
     * @return an {@link InitUploadResponse} with: the generated media ID, the storage key, a presigned upload URL and
     * any required HTTP headers
     * @throws SecurityException        if the user does not own the target journal
     * @throws IllegalArgumentException if the request is invalid
     */
    @PostMapping("init")
    public InitUploadResponse init(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody InitUploadRequest request) {
        String userId = jwt.getSubject(); // adjustable if  user id lives in another claim
        return service.initUpload(userId, request);
    }

    /**
     * Confirms completion of a media upload for the authenticated user.
     * <p>
     * This endpoint delegates to {@link MediaService#confirmUpload(String, ConfirmUploadRequest)}
     * to:
     * - Validate that the media belongs to the authenticated user.
     * - Update the media record with checksum, file size, width, height, and status.
     * - Mark the media as {@code READY} after a successful upload.
     * <p>
     * The authenticated user's ID is extracted from the {@link Jwt} principal using {@link Jwt#getSubject()}.
     * Adjust this if the user identifier resides in a different claim.
     *
     * @param jwt     the authenticated user principal (JWT token)
     * @param request the upload confirmation request containing media ID,
     *                checksum, file size, width, and height
     * @return a {@link MediaResponse} representing the finalized media record
     * @throws java.util.NoSuchElementException if the media with the given ID does not exist
     * @throws SecurityException                if the media does not belong to the authenticated user
     */
    @PostMapping("/confirm")
    public MediaResponse confirmUpload(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ConfirmUploadRequest request) {
        String userId = jwt.getSubject();
        return service.confirmUpload(userId, request);
    }

    /**
     * Retrieves a media resource by its unique identifier.
     * *
     * This endpoint delegates to {@link MediaService#getMedia(String)} to load the media metadata from the repository
     * and return it as a {@link MediaResponse}.
     *
     * @param id the unique identifier of the media (path variable)
     * @return a {@link MediaResponse} containing the media details
     * @throws java.util.NoSuchElementException if no media with the given ID exists
     */
    @GetMapping("/{id}")
    public MediaResponse get(@PathVariable String id) {
        return service.getMedia(id);
    }

    /**
     * Retrieves multiple media resources by their unique identifiers.
     * *
     * This endpoint delegates to {@link MediaService#getMediaList(List)} to fetch and return the media records
     * corresponding to the given IDs. Media that do not exist are simply ignored.
     *
     * @param ids a list of media identifiers to retrieve
     * @return a list of {@link MediaResponse} objects for the found media
     */
    @PostMapping("/batch")
    public List<MediaResponse> byIds(@RequestBody List<String> ids) {
        return service.getMediaList(ids);
    }

    /**
     * Deletes a media resource owned by the authenticated user.
     * *
     * This endpoint delegates to {@link MediaService#deleteMedia(String, String)} to:
     * - Ensure the user owns the media.
     * - Remove the object from storage (if it exists).
     * - Mark the media status as {@code DELETED}.
     * *
     * The user ID is extracted from the {@link Jwt} principal using {@link Jwt#getSubject()}. Adjust this if the user
     * identifier resides in a different claim.
     *
     * @param jwt the authenticated user principal (JWT token)
     * @param id  the unique identifier of the media to delete
     * @throws java.util.NoSuchElementException if no media with the given ID exists
     * @throws SecurityException                if the media does not belong to the authenticated user
     */
    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        String userId = jwt.getSubject();
        service.deleteMedia(id, userId);
    }

}
