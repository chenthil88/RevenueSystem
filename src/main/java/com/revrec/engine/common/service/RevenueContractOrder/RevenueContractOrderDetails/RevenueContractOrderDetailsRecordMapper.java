package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractOrderDetailsRecord}.
 */
@Component
public final class RevenueContractOrderDetailsRecordMapper implements RowMapper<RevenueContractOrderDetailsRecord> {

    @Override
    public @NonNull RevenueContractOrderDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var transactionType = rs.getString(c++);
        var revenueContractId = rs.getObject(c++, Long.class);
        var salesOrderId = rs.getObject(c++, Long.class);
        var salesOrderNumber = rs.getString(c++);
        var salesOrderDate = rs.getObject(c++, java.time.LocalDate.class);
        var sellPrice = rs.getBigDecimal(c++);
        var listPrice = rs.getBigDecimal(c++);
        var unitSellPrice = rs.getBigDecimal(c++);
        var unitListPrice = rs.getBigDecimal(c++);
        var transactionCurrency = rs.getString(c++);
        var functionalCurrency = rs.getString(c++);
        var exchangeRate = rs.getBigDecimal(c++);
        var globalexchangeRate = rs.getBigDecimal(c++);
        var exchangeRateDate = rs.getObject(c++, java.time.LocalDate.class);
        var customerNumber = rs.getString(c++);
        var customerName = rs.getString(c++);
        var itemNumber = rs.getString(c++);
        var deferAmount = rs.getBigDecimal(c++);
        var recognizeAmount = rs.getBigDecimal(c++);
        var billedDeferAmount = rs.getBigDecimal(c++);
        var billedRecognizeAmount = rs.getBigDecimal(c++);
        var bundleConfigurationId = rs.getObject(c++, Long.class);
        var orderQuantity = rs.getBigDecimal(c++);
        var invoiceQuantity = rs.getBigDecimal(c++);
        var returnQuantity = rs.getBigDecimal(c++);
        var revenueStartDate = rs.getObject(c++, java.time.LocalDate.class);
        var revenueEndDate = rs.getObject(c++, java.time.LocalDate.class);
        var duration = rs.getObject(c++, Long.class);
        var term = rs.getBigDecimal(c++);
        var bookId = rs.getObject(c++, Long.class);
        var organizationId = rs.getObject(c++, Long.class);
        var discountAmount = rs.getBigDecimal(c++);
        var discountPercentage = rs.getBigDecimal(c++);
        var bundleParentId = rs.getObject(c++, Long.class);
        var companyCode = rs.getString(c++);
        var isCancelOrder = rs.getObject(c++, Boolean.class);
        var isReturnOrder = rs.getObject(c++, Boolean.class);
        var isEligibleForAllocation = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var cumulativeCarveAmount = rs.getBigDecimal(c++);
        var cumulativeAllocatedPrice = rs.getBigDecimal(c++);
        var comments = rs.getString(c++);
        var createdBy = rs.getString(c++);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedBy = rs.getString(c++);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var productCategory = rs.getString(c++);
        var productClass = rs.getString(c++);
        var productFamily = rs.getString(c++);
        var productLine = rs.getString(c++);
        var businessUnit = rs.getString(c++);
        var contractModificationDate = rs.getObject(c++, java.time.LocalDate.class);
        var subscriptionId = rs.getObject(c++, Long.class);
        var subscriptionName = rs.getString(c++);
        var subscriptionVersion = rs.getObject(c++, Long.class);
        var subscriptionStartDate = rs.getObject(c++, java.time.LocalDate.class);
        var subscriptionEndDate = rs.getObject(c++, java.time.LocalDate.class);
        return new RevenueContractOrderDetailsRecord(
                id,
                transactionType,
                revenueContractId,
                salesOrderId,
                salesOrderNumber,
                salesOrderDate,
                sellPrice,
                listPrice,
                unitSellPrice,
                unitListPrice,
                transactionCurrency,
                functionalCurrency,
                exchangeRate,
                globalexchangeRate,
                exchangeRateDate,
                customerNumber,
                customerName,
                itemNumber,
                deferAmount,
                recognizeAmount,
                billedDeferAmount,
                billedRecognizeAmount,
                bundleConfigurationId,
                orderQuantity,
                invoiceQuantity,
                returnQuantity,
                revenueStartDate,
                revenueEndDate,
                duration,
                term,
                bookId,
                organizationId,
                discountAmount,
                discountPercentage,
                bundleParentId,
                companyCode,
                isCancelOrder,
                isReturnOrder,
                isEligibleForAllocation,
                createdPeriodId,
                cumulativeCarveAmount,
                cumulativeAllocatedPrice,
                comments,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt,
                productCategory,
                productClass,
                productFamily,
                productLine,
                businessUnit,
                contractModificationDate,
                subscriptionId,
                subscriptionName,
                subscriptionVersion,
                subscriptionStartDate,
                subscriptionEndDate
        );
    }
}
