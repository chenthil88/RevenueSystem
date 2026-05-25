package com.revrec.engine.domain.service.RevenueContractBatchDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractBatchDetailsRecord}.
 */
@Component
public final class RevenueContractBatchDetailsRecordMapper implements RowMapper<RevenueContractBatchDetailsRecord> {

    @Override
    public @NonNull RevenueContractBatchDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var batchId = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var stageRecordProcessed = rs.getObject(c++, Long.class);
        var revenueContractRecordProcessed = rs.getObject(c++, Long.class);
        var processedFlag = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdBy = rs.getString(c++);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedBy = rs.getString(c++);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new RevenueContractBatchDetailsRecord(
                id,
                batchId,
                revenueContractId,
                stageRecordProcessed,
                revenueContractRecordProcessed,
                processedFlag,
                createdPeriodId,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt
        );
    }
}
