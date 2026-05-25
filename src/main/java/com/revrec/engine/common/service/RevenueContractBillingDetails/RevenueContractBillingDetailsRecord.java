package com.revrec.engine.domain.service.RevenueContractBillingDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractBillingDetails`.
 */
public record RevenueContractBillingDetailsRecord(
        Long id,
        Long revenueContractOrderDetailsId,
        String transactionType,
        Long revenueContractId,
        Long invoiceId,
        String invoiceNumber,
        LocalDate invoiceDate,
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
        Long referenceDocoumentId,
        Boolean isCancelled,
        String bundleParentId,
        Long bundleConfigurationId
) implements RevenueContractBillingDetails, Serializable {
    public static final String TABLE_NAME = "revenueContractBillingDetails";
}
