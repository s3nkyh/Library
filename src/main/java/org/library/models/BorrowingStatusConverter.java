package org.library.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BorrowingStatusConverter implements AttributeConverter<BorrowingStatus, String> {

    @Override
    public String convertToDatabaseColumn(BorrowingStatus status) {
        return status == null ? null : status.name();
    }

    @Override
    public BorrowingStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : BorrowingStatus.valueOf(dbData);
    }
}
