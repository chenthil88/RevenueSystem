package com.revrec.engine.common.metadataservice.JournalAccount;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table {@code JournalAccountsSetup}.
 */
public record JournalAccountRecord(
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
) implements JournalAccount, Serializable {
    public static final String TABLE_NAME = "JournalAccountsSetup";
}
