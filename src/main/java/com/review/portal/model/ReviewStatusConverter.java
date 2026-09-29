package com.review.portal.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Attribute Converter to gracefully handle case-insensitive conversion between
 * ReviewStatus enum and PostgreSQL status column (e.g. 'PENDING' vs 'Pending').
 */
@Converter(autoApply = true)
public class ReviewStatusConverter implements AttributeConverter<ReviewStatus, String> {

    @Override
    public String convertToDatabaseColumn(ReviewStatus attribute) {
        if (attribute == null) {
            return ReviewStatus.PENDING.name();
        }
        return attribute.name().toUpperCase();
    }

    @Override
    public ReviewStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return ReviewStatus.PENDING;
        }
        return ReviewStatus.fromString(dbData);
    }
}
