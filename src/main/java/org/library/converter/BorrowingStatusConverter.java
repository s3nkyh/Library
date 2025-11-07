package org.library.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.library.model.BorrowingStatus;

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
