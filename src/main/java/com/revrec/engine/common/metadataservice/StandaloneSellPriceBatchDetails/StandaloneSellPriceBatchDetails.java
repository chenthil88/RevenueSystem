package com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Row shape for {@link StandaloneSellPriceBatchDetailsRecord}.
 */
public interface StandaloneSellPriceBatchDetails {

    Long id();

    String tenantId();

    Long batchId();

    Long standaloneSellPriceTemplateId();

    String attributeField1();

    String attributeField2();

    String attributeField3();

    String attributeField4();

    String attributeField5();

    String attributeField6();

    String attributeField7();

    String attributeField8();

    String attributeField9();

    String attributeField10();

    BigDecimal aboveSspPercentage();

    BigDecimal sspPercentage();

    BigDecimal belowSspPercentage();

    BigDecimal aboveSspPrice();

    BigDecimal sspPrice();

    BigDecimal belowSspPrice();

    Boolean isActive();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    String createdBy();

    String updatedBy();
}
