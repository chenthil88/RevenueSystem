package com.revrec.engine.domain.metadataservice.Calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link CalendarRecord}.
 */
public interface Calendar {

    Long periodId();

    String tenantId();

    String periodName();

    LocalDate quarterStartDate();

    LocalDate quarterEndDate();

    LocalDate monthStartDate();

    LocalDate monthEndDate();

    LocalDate yearStartDate();

    LocalDate yearEndDate();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
