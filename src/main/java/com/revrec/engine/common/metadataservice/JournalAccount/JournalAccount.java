package com.revrec.engine.common.metadataservice.JournalAccount;

import java.time.LocalDateTime;

/**
 * Row shape for {@link JournalAccountRecord}.
 */
public interface JournalAccount {

    Long id();

    default String tenantId() {
        return null;
    }

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
