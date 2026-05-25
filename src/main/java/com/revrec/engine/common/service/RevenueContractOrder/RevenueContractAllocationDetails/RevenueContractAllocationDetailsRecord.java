package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractAllocationDetails`.
 */
public record RevenueContractAllocationDetailsRecord(
        Long id,
        Long revenueContractId,
        BigDecimal extendedSspPrice,
        String allocationCurrency,
        BigDecimal exchangeRate,
        BigDecimal globalexchangeRate,
        LocalDate exchangeRateDate,
        BigDecimal carveAmount,
        BigDecimal cumulativeReleasedAmount,
        BigDecimal cumulativeUnReleasedAmount,
        BigDecimal transactionPrice,
        BigDecimal allocatedPrice,
        BigDecimal netQuantity,
        BigDecimal term,
        Long bookId,
        Long organizationId,
        BigDecimal transactionFunctionalPrice,
        Long sspTemplateId,
        Long sspId,
        String sspType,
        BigDecimal sspPrice,
        BigDecimal sspPercentage,
        BigDecimal aboveSspPrice,
        BigDecimal belowSspPrice,
        BigDecimal belowMidPercentage,
        BigDecimal aboveMidPercentage,
        Boolean isCancelOrder,
        Boolean isReturnOrder,
        Long createdPeriodId,
        BigDecimal cumulativeCarveAmount,
        BigDecimal cumulativeAllocatedPrice,
        String comments,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt
) implements RevenueContractAllocationDetails, Serializable {
    public static final String TABLE_NAME = "revenueContractAllocationDetails";
}
