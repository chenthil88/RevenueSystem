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
        var revenueContractId = rs.getObject(c++, Long.class);
        var version = rs.getObject(c++, Long.class);
        var totalSellPrice = rs.getBigDecimal(c++);
        var totalListPrice = rs.getBigDecimal(c++);
        var totalCarveAmount = rs.getBigDecimal(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var initialContractModificationDate = rs.getObject(c++, java.time.LocalDate.class);
        var contractModificationDate = rs.getObject(c++, java.time.LocalDate.class);
        var isRevenueContractPosted = rs.getObject(c++, Boolean.class);
        var allocationTreatment = rs.getString(c++);
        var createdBy = rs.getString(c++);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedBy = rs.getString(c++);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var isActive = rs.getObject(c++, Boolean.class);
        return new RevenueContractHeaderRecord(
                revenueContractId,
                version,
                totalSellPrice,
                totalListPrice,
                totalCarveAmount,
                createdPeriodId,
                initialContractModificationDate,
                contractModificationDate,
                isRevenueContractPosted,
                allocationTreatment,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt,
                isActive
        );
    }
}
