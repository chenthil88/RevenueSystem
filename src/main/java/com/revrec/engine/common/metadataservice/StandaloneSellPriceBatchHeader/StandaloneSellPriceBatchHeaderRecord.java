package com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchHeader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `StandaloneSellPriceBatchHeader`.
 */
public record StandaloneSellPriceBatchHeaderRecord(
        Long id,
        Long standaloneSellPriceTemplateId,
        String name,
        String description,
        String sspType,
        LocalDate effectiveFromDate,
        LocalDate effectiveToDate,
        String status,
        Long createdPeriodId,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        Boolean isActive
) implements StandaloneSellPriceBatchHeader, Serializable {
    public static final String TABLE_NAME = "StandaloneSellPriceBatchHeader";
}
