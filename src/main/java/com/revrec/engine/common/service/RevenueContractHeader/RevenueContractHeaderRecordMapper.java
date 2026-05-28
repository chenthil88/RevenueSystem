package com.revrec.engine.domain.service.RevenueContractHeader;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractHeaderRecord}.
 */
@Component
public final class RevenueContractHeaderRecordMapper implements RowMapper<RevenueContractHeaderRecord> {

    @Override
    public @NonNull RevenueContractHeaderRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var record = new RevenueContractHeaderRecord();
        record.setRevenueContractId(rs.getObject(c++, Long.class));
        record.setVersion(rs.getObject(c++, Long.class));
        record.setTotalSellPrice(rs.getBigDecimal(c++));
        record.setTotalListPrice(rs.getBigDecimal(c++));
        record.setTotalCarveAmount(rs.getBigDecimal(c++));
        record.setCreatedPeriodId(rs.getObject(c++, Long.class));
        record.setInitialContractModificationDate(rs.getObject(c++, java.time.LocalDate.class));
        record.setContractModificationDate(rs.getObject(c++, java.time.LocalDate.class));
        record.setIsRevenueContractPosted(rs.getObject(c++, Boolean.class));
        record.setAllocationTreatment(rs.getString(c++));
        record.setCreatedBy(rs.getString(c++));
        record.setCreatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        record.setUpdatedBy(rs.getString(c++));
        record.setUpdatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        record.setIsAllocationInitialEntryCreated(rs.getObject(c++, Boolean.class));
        record.setIsActive(rs.getObject(c++, Boolean.class));
        record.setIsEligibleForRelease(false);
        record.setIsEligibleForRecalculation(false);
        return record;
    }
}
