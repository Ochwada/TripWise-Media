package com.tripwise.tripmedia.service;


import com.tripwise.tripmedia.dto.ConfirmUploadRequest;
import com.tripwise.tripmedia.dto.InitUploadRequest;
import com.tripwise.tripmedia.dto.InitUploadResponse;
import com.tripwise.tripmedia.dto.MediaResponse;
import com.tripwise.tripmedia.model.Media;
import com.tripwise.tripmedia.model.MediaStatus;
import com.tripwise.tripmedia.repository.MediaRepository;
import com.tripwise.tripmedia.service.client.JournalClient;
import com.tripwise.tripmedia.service.client.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.service.client
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 18:16
 * Description : Service layer for managing media objects within TripJournal.
 * -  Provides functionality to initialize uploads, interact with storage backends and persist metadata for uploaded files.
 * ================================================================
 */
@Service
@RequiredArgsConstructor
public class MediaService {
    /**
     * Responsibilities:
     * - Verifies journal ownership using {@link JournalClient}.
     * - Generates presigned upload URLs via {@link StorageClient}.
     * - Persists media metadata with {@link MediaRepository}.
     * - Returns upload instructions to clients (upload URL, headers, etc.).
     * Lifecycle:
     * - Validate user’s right to upload into a journal.
     * - Create unique media ID + storage key.
     * - Request presigned PUT URL from storage.
     * - Persist media entry in {@code MediaRepository} with status {@code UPLOADING}.
     * - Return initialization response to client.
     */

    private final MediaRepository repository;
    private final StorageClient storageClient;
    private final JournalClient journals;

    /**
     * Initializes a new media upload for a given user and journal.
     * This method:
     * - Checks that the user owns the specified journal.
     * - Generates a new media ID and canonical storage key.
     * - Creates a presigned upload URL via {@link StorageClient}.
     * - Stores the media metadata in {@link MediaRepository}.
     * - Returns an {@link InitUploadResponse} with upload instructions.
     *
     * @param userId  the ID of the authenticated user performing the upload
     * @param request details of the upload request (journalId, file name, MIME type, file size)
     * @return an {@link InitUploadResponse} containing:
     * - media ID
     * - storage key
     * - presigned upload URL
     * - any required HTTP headers
     * @throws SecurityException        if the user does not own the target journal
     * @throws IllegalArgumentException if the request is invalid
     */
    public InitUploadResponse initUpload(String userId, InitUploadRequest request) {

        // Ensure the user is allowed to upload into this journal
        journals.assertOwnership(request.getJournalId(), userId);

        // Generate unique media identifier + storage key
        String id = UUID.randomUUID().toString();
        String key = userId + "/" + id + "/" + request.getFileName();

        // Request presigned PUT URL from storage backend
        var pre = storageClient.presignPut(key, request.getMimeType(), request.getBytes());

        // Persist media metadata in repository with UPLOADING status
        var media = Media.builder()
                .id(id)
                .userId(userId)
                .journalId(request.getJournalId())
                .fileName(request.getFileName())
                .mimeType(request.getMimeType())
                .bytes(request.getBytes())
                .storageKey(key)
                .cdnUrl(storageClient.publicUrl(key))
                .status(MediaStatus.UPLOADING)
                .build();

        repository.save(media);

        // Return presigned upload instructions to client
        return InitUploadResponse.builder()
                .mediaId(id)
                .storageKey(pre.storageKey())
                .uploadUrl(pre.url().toString())
                .headers(pre.headers())
                .build();
    }


    /**
     * Confirms completion of an upload and finalizes the associated media record.
     * *
     * This method performs the following steps:
     * - Retrieves the {@code Media} entity by its ID.
     * - Validates that the given user is the owner of the media.
     * - Updates metadata such as checksum, file size, width, height, and status.
     * - Persists the updated entity back into {@link MediaRepository}.
     * - Returns a {@link MediaResponse} representation of the updated media.
     * *
     * After this call, the media status transitions from {@code UPLOADING} to {@code READY}.
     *
     * @param userId  the ID of the user confirming the upload; must match the media's owner
     * @param request the confirmation payload containing media ID, checksum, file size, width, and height
     * @return a {@link MediaResponse} containing the finalized media details
     * @throws NoSuchElementException if no media with the given ID exists
     * @throws SecurityException      if the user does not own the media
     */
    public MediaResponse confirmUpload(String userId, ConfirmUploadRequest request) {
        var m = repository
                .findById(request.getMediaId())
                .orElseThrow();

        if (!Objects.equals(m.getUserId(), userId)) {
            throw new SecurityException("Not Owner");
        }
        m.setChecksum(request.getChecksum());
        m.setBytes(request.getBytes());
        m.setWidth(request.getWidth());
        m.setHeight(request.getHeight());
        m.setStatus(MediaStatus.READY);

        repository.save(m);

        return MediaResponse.from(m);
    }


    /**
     * Retrieves a media record by its unique identifier.
     * *
     * The method queries the {@link MediaRepository} for the given media ID, converts the entity into a
     * {@link MediaResponse}, and returns it. If the media does not exist, an exception is thrown.
     *
     * @param id the unique identifier of the media
     * @return a {@link MediaResponse} representing the media entity
     * @throws NoSuchElementException if no media with the given ID exists
     */
    public MediaResponse getMedia(String id) {
        return repository.findById(id)
                .map(MediaResponse::from)
                .orElseThrow();
    }

    /**
     * Retrieves a list of media records for the given set of IDs.
     * *
     * This method performs a batch lookup in the {@link MediaRepository} converts each found entity into a
     * {@link MediaResponse}, and return them as a list. If some IDs are not found, only the existing media
     * entries are returned.
     *
     * @param ids a list of media identifiers to fetch
     * @return a list of {@link MediaResponse} objects corresponding to the found media
     */
    public List<MediaResponse> getMediaList(List<String> ids) {
        return repository.findByIdIn(ids)
                .stream()
                .map(MediaResponse::from)
                .toList();
    }


    /**
     * Deletes a media object owned by the given user.
     * *
     * This method:
     * - Fetches the media entity by its ID.
     * - Validates that the requesting user is the owner of the media.
     * - If a storage key is present, removes the object from the storage backend via {@link StorageClient}.
     * - Updates the media status to {@code DELETED} and persists the change.
     * <p>
     * If no media with the given ID exists, a {@link NoSuchElementException} is thrown. If the user does not
     * own the media, a {@link SecurityException} is thrown.
     *
     * @param id     the unique identifier of the media to delete
     * @param userId the ID of the user attempting the deletion; must match the media owner
     * @throws NoSuchElementException if no media with the given ID exists
     * @throws SecurityException      if the user does not own the media
     */
    public void deleteMedia(String id, String userId) {
        var m = repository.findById(id).orElseThrow();

        if (!Objects.equals(m.getUserId(), userId)) {
            throw new SecurityException("Not Owner");
        }

        if (m.getStorageKey() != null) {
            storageClient.deleteObject(m.getStorageKey());
            m.setStatus(MediaStatus.DELETED);
            repository.save(m);
        }
    }

}
