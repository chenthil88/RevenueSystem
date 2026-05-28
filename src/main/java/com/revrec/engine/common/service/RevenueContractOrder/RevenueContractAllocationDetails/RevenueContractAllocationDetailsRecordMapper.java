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
        var record = new RevenueContractAllocationDetailsRecord();
        record.setId(rs.getObject(c++, Long.class));
        record.setRevenueContractId(rs.getObject(c++, Long.class));
        record.setExtendedSspPrice(rs.getBigDecimal(c++));
        record.setAllocationCurrency(rs.getString(c++));
        record.setExchangeRate(rs.getBigDecimal(c++));
        record.setGlobalexchangeRate(rs.getBigDecimal(c++));
        record.setExchangeRateDate(rs.getObject(c++, java.time.LocalDate.class));
        record.setCarveAmount(rs.getBigDecimal(c++));
        record.setCumulativeReleasedAmount(rs.getBigDecimal(c++));
        record.setCumulativeUnReleasedAmount(rs.getBigDecimal(c++));
        record.setTransactionPrice(rs.getBigDecimal(c++));
        record.setAllocatedPrice(rs.getBigDecimal(c++));
        record.setNetQuantity(rs.getBigDecimal(c++));
        record.setTerm(rs.getBigDecimal(c++));
        record.setBookId(rs.getObject(c++, Long.class));
        record.setOrganizationId(rs.getObject(c++, Long.class));
        record.setTransactionFunctionalPrice(rs.getBigDecimal(c++));
        record.setSspTemplateId(rs.getObject(c++, Long.class));
        record.setSspId(rs.getObject(c++, Long.class));
        record.setSspType(rs.getString(c++));
        record.setSspPrice(rs.getBigDecimal(c++));
        record.setSspPercentage(rs.getBigDecimal(c++));
        record.setAboveSspPrice(rs.getBigDecimal(c++));
        record.setBelowSspPrice(rs.getBigDecimal(c++));
        record.setBelowMidPercentage(rs.getBigDecimal(c++));
        record.setAboveMidPercentage(rs.getBigDecimal(c++));
        record.setIsCancelOrder(rs.getObject(c++, Boolean.class));
        record.setIsReturnOrder(rs.getObject(c++, Boolean.class));
        record.setCreatedPeriodId(rs.getObject(c++, Long.class));
        record.setCumulativeCarveAmount(rs.getBigDecimal(c++));
        record.setCumulativeAllocatedPrice(rs.getBigDecimal(c++));
        record.setComments(rs.getString(c++));
        record.setCreatedBy(rs.getString(c++));
        record.setCreatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        record.setUpdatedBy(rs.getString(c++));
        record.setUpdatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        return record;
    }
}
