package com.revrec.engine.domain.metadataservice.CurrentOpenPeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link CurrentOpenPeriodRecord}.
 */
public interface CurrentOpenPeriod {

    Long openPeriodId();

    String tenantId();

    String openPeriodName();

    LocalDate openPeriodStartDate();

    LocalDate openPeriodEndDate();

    String openPeriodStatus();

    LocalDateTime openPeriodCreatedAt();

    LocalDateTime openPeriodUpdatedAt();

    Long organizationId();

    Long bookId();
}
