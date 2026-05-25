package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractAllocationDetailsRecord}.
 */
public interface RevenueContractAllocationDetails {

    Long id();

    String tenantId();

    Long revenueContractId();

    BigDecimal extendedSspPrice();

    String allocationCurrency();

    BigDecimal exchangeRate();

    BigDecimal globalexchangeRate();

    LocalDate exchangeRateDate();

    BigDecimal carveAmount();

    BigDecimal unreleasedCarveAmount();

    BigDecimal cumulativeReleasedAmount();

    BigDecimal cumulativeUnReleasedAmount();

    BigDecimal transactionPrice();

    BigDecimal allocatedPrice();

    BigDecimal netQuantity();

    BigDecimal term();

    Long bookId();

    Long organizationId();

    BigDecimal transactionFunctionalPrice();

    Long sspTemplateId();

    Long sspId();

    String sspType();

    BigDecimal sspPrice();

    BigDecimal sspPercentage();

    BigDecimal aboveSspPrice();

    BigDecimal belowSspPrice();

    BigDecimal belowMidPercentage();

    BigDecimal aboveMidPercentage();

    Boolean isCancelOrder();

    Boolean isReturnOrder();

    Long createdPeriodId();

    BigDecimal cumulativeCarveAmount();

    BigDecimal cumulativeAllocatedPrice();

    String comments();

    String createdBy();

    LocalDateTime createdAt();

    String updatedBy();

    LocalDateTime updatedAt();
}
