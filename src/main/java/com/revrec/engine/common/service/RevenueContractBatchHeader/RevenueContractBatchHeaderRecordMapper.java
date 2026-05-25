package com.revrec.engine.domain.service.RevenueContractBatchHeader;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractBatchHeaderRecord}.
 */
@Component
public final class RevenueContractBatchHeaderRecordMapper implements RowMapper<RevenueContractBatchHeaderRecord> {

    @Override
    public @NonNull RevenueContractBatchHeaderRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var batchId = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var revenueContractGroupingTemplateId = rs.getObject(c++, Long.class);
        var status = rs.getString(c++);
        var stageRecordProcessed = rs.getObject(c++, Long.class);
        var revenueContractRecordProcessed = rs.getObject(c++, Long.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdBy = rs.getString(c++);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedBy = rs.getString(c++);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new RevenueContractBatchHeaderRecord(
                batchId,
                name,
                description,
                revenueContractGroupingTemplateId,
                status,
                stageRecordProcessed,
                revenueContractRecordProcessed,
                createdPeriodId,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt
        );
    }
}
