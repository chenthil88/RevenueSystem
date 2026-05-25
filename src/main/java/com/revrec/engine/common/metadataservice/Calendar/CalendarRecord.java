package com.revrec.engine.domain.metadataservice.Calendar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `Calendar`.
 */
public record CalendarRecord(
        Long periodId,
        String periodName,
        LocalDate quarterStartDate,
        LocalDate quarterEndDate,
        LocalDate monthStartDate,
        LocalDate monthEndDate,
        LocalDate yearStartDate,
        LocalDate yearEndDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Calendar, Serializable {
    public static final String TABLE_NAME = "Calendar";
}
