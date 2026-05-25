package com.revrec.engine.domain.service.RevenueContractBatchHeader;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter for BatchStatus enum
 * Converts between database string codes and Java enum values
 */
@Converter(autoApply = true)
public class BatchStatusConverter implements AttributeConverter<BatchStatus, String> {

    @Override
    public String convertToDatabaseColumn(BatchStatus status) {
        if (status == null) {
            return BatchStatus.NEW.getCode();
        }
        return status.getCode();
    }

    @Override
    public String convertToEntityAttribute(String code) {
        if (code == null || code.isBlank()) {
            return BatchStatus.NEW.getCode();
        }
        return BatchStatus.fromCode(code).getCode();
    }
}
