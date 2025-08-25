package com.tripwise.tripmedia.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.model
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 17:00
 * Description : Represents the lifecycle status of a media resource.
 * ================================================================
 */
public enum MediaStatus {
    UPLOADING("uploading"),
    READY("Ready"),
    FAILED("Failed"),
    DELETED("Deleted");

    /**
     * Human-readable label for the status, used in serialization/deserialization.
     */
    private final String label;

    /**
     * Constructs a new {@link MediaStatus} with the given label.
     *
     * @param label the label used for serialization
     */
    MediaStatus(String label) {
        this.label = label;
    }

    /**
     * Returns the label of the status for JSON serialization.
     *
     * @return the human-readable label
     */
    @JsonValue                 // serialize as "Uploading", "Ready", ...
    public String getLabel() {
        return label;
    }

    /**
     * Returns the string representation of the status (same as {@link #getLabel()}).
     *
     * @return the status label
     */
    @Override
    public String toString() {
        return label;
    }

    /**
     * Creates a {@link MediaStatus} from a given string value.
     * *
     * This method is case-insensitive and accepts both enum names (e.g., "UPLOADING")
     * and labels (e.g., "uploading").
     *
     * @param value the input string value
     * @return the corresponding {@link MediaStatus}
     * @throws IllegalArgumentException if no matching status exists
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING) // allow parsing those labels back
    public static MediaStatus fromValue(String value) {
        for (var s : values()) {
            if (
                    s.label.equalsIgnoreCase(value) ||
                            s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown MediaStatus: " + value);
    }


}
