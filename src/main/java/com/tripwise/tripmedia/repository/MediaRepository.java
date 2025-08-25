package com.tripwise.tripmedia.repository;

import com.tripwise.tripmedia.model.Media;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.repository
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 17:38
 * Description : Repository interface for accessing and managing {@link Media} documents in MongoDB.
 * ================================================================
 */
public interface MediaRepository extends MongoRepository<Media, String> {

    /**
     * Provides built-in CRUD operations such as:
     * - {@code save(Media media)}
     * - {@code findById(String id)}
     * - {@code findAll()}
     * - {@code deleteById(String id)}
     * */

    /** -------------------------------------------------------------------------------------------
     * Additional query methods are defined by following Spring Data's derived query method conventions
     -------------------------------------------------------------------------------------------*/

    /**
     * Finds all {@link Media} documents whose IDs are contained in the given collection.
     *
     * @param ids a collection of media IDs
     * @return a list of matching {@link Media} documents
     */
    List<Media> findByIdIn(Collection<String> ids);
}
