package com.revrec.engine.domain.metadataservice.JournalAccountsSetup;

import java.time.LocalDateTime;

/**
 * Row shape for {@link JournalAccountsSetupRecord}.
 */
public interface JournalAccountsSetup {

    Long id();

    String tenantId();

    String name();

    String description();

    String segmentPosition1();

    String segmentPosition2();

    String segmentPosition3();

    String segmentPosition4();

    String segmentPosition5();

    String segmentPosition6();

    String segmentPosition7();

    String segmentPosition8();

    String segmentPosition9();

    String segmentPosition10();

    Boolean isActive();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
