package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractOrderDetails`.
 */
public record RevenueContractOrderDetailsRecord(
        Long id,
        String transactionType,
        Long revenueContractId,
        Long salesOrderId,
        String salesOrderNumber,
        LocalDate salesOrderDate,
        BigDecimal sellPrice,
        BigDecimal listPrice,
        BigDecimal unitSellPrice,
        BigDecimal unitListPrice,
        String transactionCurrency,
        String functionalCurrency,
        BigDecimal exchangeRate,
        BigDecimal globalexchangeRate,
        LocalDate exchangeRateDate,
        String customerNumber,
        String customerName,
        String itemNumber,
        BigDecimal deferAmount,
        BigDecimal recognizeAmount,
        BigDecimal billedDeferAmount,
        BigDecimal billedRecognizeAmount,
        Long bundleConfigurationId,
        BigDecimal orderQuantity,
        BigDecimal invoiceQuantity,
        BigDecimal returnQuantity,
        LocalDate revenueStartDate,
        LocalDate revenueEndDate,
        Long duration,
        BigDecimal term,
        Long bookId,
        Long organizationId,
        BigDecimal discountAmount,
        BigDecimal discountPercentage,
        Long bundleParentId,
        String companyCode,
        Boolean isCancelOrder,
        Boolean isReturnOrder,
        Boolean isEligibleForAllocation,
        Long createdPeriodId,
        BigDecimal cumulativeCarveAmount,
        BigDecimal cumulativeAllocatedPrice,
        String comments,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        String productCategory,
        String productClass,
        String productFamily,
        String productLine,
        String businessUnit,
        LocalDate contractModificationDate,
        Long subscriptionId,
        String subscriptionName,
        Long subscriptionVersion,
        LocalDate subscriptionStartDate,
        LocalDate subscriptionEndDate
) implements RevenueContractOrderDetails, Serializable {
    public static final String TABLE_NAME = "revenueContractOrderDetails";
}
