package com.revrec.engine.domain.service.RevenueContractBillingDetails;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Row shape for {@link RevenueContractBillingDetailsRecord}.
 */
public interface RevenueContractBillingDetails {

    Long id();

    String tenantId();

    Long revenueContractOrderDetailsId();

    String transactionType();

    Long revenueContractId();

    Long invoiceId();

    String invoiceNumber();

    LocalDate invoiceDate();

    BigDecimal sellPrice();

    BigDecimal listPrice();

    BigDecimal unitSellPrice();

    BigDecimal unitListPrice();

    String transactionCurrency();

    String functionalCurrency();

    BigDecimal exchangeRate();

    BigDecimal globalexchangeRate();

    LocalDate exchangeRateDate();

    String customerNumber();

    String customerName();

    String itemNumber();

    BigDecimal deferAmount();

    BigDecimal recognizeAmount();

    Long referenceDocoumentId();

    Boolean isCancelled();

    String bundleParentId();

    Long bundleConfigurationId();
}
