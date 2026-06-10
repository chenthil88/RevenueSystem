package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails;

import com.revrec.engine.common.math.ChargebeeDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractAllocationDetailsRecord}.
 */
public interface RevenueContractAllocationDetails {

    Long id();

    String tenantId();

    Long revenueContractId();

    ChargebeeDecimal extendedSspPrice();

    String allocationCurrency();

    ChargebeeDecimal exchangeRate();

    ChargebeeDecimal globalexchangeRate();

    LocalDate exchangeRateDate();

    ChargebeeDecimal carveAmount();

    ChargebeeDecimal unreleasedCarveAmount();

    ChargebeeDecimal cumulativeReleasedAmount();

    ChargebeeDecimal cumulativeUnReleasedAmount();

    ChargebeeDecimal transactionPrice();

    ChargebeeDecimal postedPercentage();

    ChargebeeDecimal allocatedPrice();

    ChargebeeDecimal netQuantity();

    ChargebeeDecimal term();

    Long bookId();

    Long organizationId();

    ChargebeeDecimal transactionFunctionalPrice();

    Long sspTemplateId();

    Long sspId();

    String sspType();

    ChargebeeDecimal sspPrice();

    ChargebeeDecimal sspPercentage();

    ChargebeeDecimal aboveSspPrice();

    ChargebeeDecimal belowSspPrice();

    ChargebeeDecimal belowMidPercentage();

    ChargebeeDecimal aboveMidPercentage();

    Boolean isCancelOrder();

    Boolean isReturnOrder();

    Long createdPeriodId();

    ChargebeeDecimal cumulativeCarveAmount();

    ChargebeeDecimal cumulativeAllocatedPrice();

    String comments();

    String createdBy();

    LocalDateTime createdAt();

    String updatedBy();

    LocalDateTime updatedAt();
}
