package com.revrec.engine.domain.service.RevenueContractBillingDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractBillingDetailsRecord}.
 */
@Component
public final class RevenueContractBillingDetailsRecordMapper implements RowMapper<RevenueContractBillingDetailsRecord> {

    @Override
    public @NonNull RevenueContractBillingDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractOrderDetailsId = rs.getObject(c++, Long.class);
        var transactionType = rs.getString(c++);
        var revenueContractId = rs.getObject(c++, Long.class);
        var invoiceId = rs.getObject(c++, Long.class);
        var invoiceNumber = rs.getString(c++);
        var invoiceDate = rs.getObject(c++, java.time.LocalDate.class);
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
        var referenceDocoumentId = rs.getObject(c++, Long.class);
        var isCancelled = rs.getObject(c++, Boolean.class);
        var bundleParentId = rs.getString(c++);
        var bundleConfigurationId = rs.getObject(c++, Long.class);
        return new RevenueContractBillingDetailsRecord(
                id,
                revenueContractOrderDetailsId,
                transactionType,
                revenueContractId,
                invoiceId,
                invoiceNumber,
                invoiceDate,
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
                referenceDocoumentId,
                isCancelled,
                bundleParentId,
                bundleConfigurationId
        );
    }
}
