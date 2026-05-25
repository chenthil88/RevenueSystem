package com.revrec.engine.domain.metadataservice.Currency;

import java.time.LocalDateTime;

/**
 * Row shape for {@link CurrencyRecord}.
 */
public interface Currency {

    Long id();

    String tenantId();

    String currencyName();

    String currencyCode();

    Long currencyRounding();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
