package com.revrec.engine.domain.service.RevenueContractGroupDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractGroupDetailsRecord}.
 */
@Component
public final class RevenueContractGroupDetailsRecordMapper implements RowMapper<RevenueContractGroupDetailsRecord> {

    @Override
    public @NonNull RevenueContractGroupDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var groupingValue = rs.getString(c++);
        var closedFlag = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new RevenueContractGroupDetailsRecord(
                id,
                revenueContractId,
                groupingValue,
                closedFlag,
                createdPeriodId,
                isActive,
                createdAt,
                updatedAt
        );
    }
}
