package com.review.portal.model;

/**
 * Enumeration representing the lifecycle states of an employee performance review.
 */
public enum ReviewStatus {
    PENDING,
    Pending,
    DRAFT,
    SUBMITTED,
    UNDER_REVIEW,
    APPROVED,
    REJECTED;

    public static ReviewStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            return PENDING;
        }
        for (ReviewStatus s : values()) {
            if (s.name().equalsIgnoreCase(status.trim())) {
                return s;
            }
        }
        return PENDING;
    }

    public String getDisplayName() {
        if (this == APPROVED) return "Approved";
        if (this == REJECTED) return "Rejected";
        if (this == UNDER_REVIEW) return "Under Review";
        if (this == DRAFT) return "Draft";
        return "Pending";
    }
}
