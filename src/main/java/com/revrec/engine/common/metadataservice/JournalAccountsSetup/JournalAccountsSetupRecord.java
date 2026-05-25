package com.revrec.engine.domain.metadataservice.JournalAccountsSetup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `JournalAccountsSetup`.
 */
public record JournalAccountsSetupRecord(
        Long id,
        String name,
        String description,
        String segmentPosition1,
        String segmentPosition2,
        String segmentPosition3,
        String segmentPosition4,
        String segmentPosition5,
        String segmentPosition6,
        String segmentPosition7,
        String segmentPosition8,
        String segmentPosition9,
        String segmentPosition10,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements JournalAccountsSetup, Serializable {
    public static final String TABLE_NAME = "JournalAccountsSetup";
}
