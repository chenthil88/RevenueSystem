package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractAllocationDetailsRecord}.
 */
@Component
public final class RevenueContractAllocationDetailsRecordMapper implements RowMapper<RevenueContractAllocationDetailsRecord> {

    @Override
    public @NonNull RevenueContractAllocationDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var extendedSspPrice = rs.getBigDecimal(c++);
        var allocationCurrency = rs.getString(c++);
        var exchangeRate = rs.getBigDecimal(c++);
        var globalexchangeRate = rs.getBigDecimal(c++);
        var exchangeRateDate = rs.getObject(c++, java.time.LocalDate.class);
        var carveAmount = rs.getBigDecimal(c++);
        var cumulativeReleasedAmount = rs.getBigDecimal(c++);
        var cumulativeUnReleasedAmount = rs.getBigDecimal(c++);
        var transactionPrice = rs.getBigDecimal(c++);
        var allocatedPrice = rs.getBigDecimal(c++);
        var netQuantity = rs.getBigDecimal(c++);
        var term = rs.getBigDecimal(c++);
        var bookId = rs.getObject(c++, Long.class);
        var organizationId = rs.getObject(c++, Long.class);
        var transactionFunctionalPrice = rs.getBigDecimal(c++);
        var sspTemplateId = rs.getObject(c++, Long.class);
        var sspId = rs.getObject(c++, Long.class);
        var sspType = rs.getString(c++);
        var sspPrice = rs.getBigDecimal(c++);
        var sspPercentage = rs.getBigDecimal(c++);
        var aboveSspPrice = rs.getBigDecimal(c++);
        var belowSspPrice = rs.getBigDecimal(c++);
        var belowMidPercentage = rs.getBigDecimal(c++);
        var aboveMidPercentage = rs.getBigDecimal(c++);
        var isCancelOrder = rs.getObject(c++, Boolean.class);
        var isReturnOrder = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var cumulativeCarveAmount = rs.getBigDecimal(c++);
        var cumulativeAllocatedPrice = rs.getBigDecimal(c++);
        var comments = rs.getString(c++);
        var createdBy = rs.getString(c++);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedBy = rs.getString(c++);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new RevenueContractAllocationDetailsRecord(
                id,
                revenueContractId,
                extendedSspPrice,
                allocationCurrency,
                exchangeRate,
                globalexchangeRate,
                exchangeRateDate,
                carveAmount,
                cumulativeReleasedAmount,
                cumulativeUnReleasedAmount,
                transactionPrice,
                allocatedPrice,
                netQuantity,
                term,
                bookId,
                organizationId,
                transactionFunctionalPrice,
                sspTemplateId,
                sspId,
                sspType,
                sspPrice,
                sspPercentage,
                aboveSspPrice,
                belowSspPrice,
                belowMidPercentage,
                aboveMidPercentage,
                isCancelOrder,
                isReturnOrder,
                createdPeriodId,
                cumulativeCarveAmount,
                cumulativeAllocatedPrice,
                comments,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt
        );
    }
}
