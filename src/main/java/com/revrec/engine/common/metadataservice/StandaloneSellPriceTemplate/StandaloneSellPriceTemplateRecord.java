package com.revrec.engine.domain.metadataservice.StandaloneSellPriceTemplate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `StandaloneSellPriceTemplate`.
 */
public record StandaloneSellPriceTemplateRecord(
        Long id,
        String name,
        String description,
        String stratificationFields,
        String applyFieldName,
        Long createdPeriodId,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements StandaloneSellPriceTemplate, Serializable {
    public static final String TABLE_NAME = "StandaloneSellPriceTemplate";
}
