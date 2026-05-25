package com.revrec.engine.domain.metadataservice.Currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `Currency`.
 */
public record CurrencyRecord(
        Long id,
        String currencyName,
        String currencyCode,
        Long currencyRounding,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Currency, Serializable {
    public static final String TABLE_NAME = "Currency";
}
