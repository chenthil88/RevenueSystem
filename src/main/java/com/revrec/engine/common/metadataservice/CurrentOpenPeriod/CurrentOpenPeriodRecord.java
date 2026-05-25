package com.revrec.engine.domain.metadataservice.CurrentOpenPeriod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `CurrentOpenPeriod`.
 */
public record CurrentOpenPeriodRecord(
        Long openPeriodId,
        String openPeriodName,
        LocalDate openPeriodStartDate,
        LocalDate openPeriodEndDate,
        String openPeriodStatus,
        LocalDateTime openPeriodCreatedAt,
        LocalDateTime openPeriodUpdatedAt,
        Long organizationId,
        Long bookId
) implements CurrentOpenPeriod, Serializable {
    public static final String TABLE_NAME = "CurrentOpenPeriod";
}
