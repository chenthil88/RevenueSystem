package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails;

import com.revrec.engine.common.math.ChargebeeDecimal;
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
        record.setExtendedSspPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setAllocationCurrency(rs.getString(c++));
        record.setExchangeRate(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setGlobalexchangeRate(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setExchangeRateDate(rs.getObject(c++, java.time.LocalDate.class));
        record.setCarveAmount(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setUnreleasedCarveAmount(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setCumulativeReleasedAmount(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setCumulativeUnReleasedAmount(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setTransactionPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setPostedPercentage(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setAllocatedPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setNetQuantity(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setTerm(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setBookId(rs.getObject(c++, Long.class));
        record.setOrganizationId(rs.getObject(c++, Long.class));
        record.setTransactionFunctionalPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setSspTemplateId(rs.getObject(c++, Long.class));
        record.setSspId(rs.getObject(c++, Long.class));
        record.setSspType(rs.getString(c++));
        record.setSspPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setSspPercentage(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setAboveSspPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setBelowSspPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setBelowMidPercentage(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setAboveMidPercentage(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setIsCancelOrder(rs.getObject(c++, Boolean.class));
        record.setIsReturnOrder(rs.getObject(c++, Boolean.class));
        record.setCreatedPeriodId(rs.getObject(c++, Long.class));
        record.setCumulativeCarveAmount(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setCumulativeAllocatedPrice(ChargebeeDecimal.of(rs.getBigDecimal(c++)));
        record.setComments(rs.getString(c++));
        record.setCreatedBy(rs.getString(c++));
        record.setCreatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        record.setUpdatedBy(rs.getString(c++));
        record.setUpdatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        return record;
    }
}
