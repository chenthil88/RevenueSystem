package com.revrec.engine.domain.metadataservice.RevenueContractGroupingHierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractGroupingHierarchyRecord}.
 */
@Component
public final class RevenueContractGroupingHierarchyRecordMapper implements RowMapper<RevenueContractGroupingHierarchyRecord> {

    @Override
    public @NonNull RevenueContractGroupingHierarchyRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var sequence = rs.getObject(c++, Long.class);
        var revenueContractGroupingTemplateId = rs.getObject(c++, Long.class);
        var groupingFields = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new RevenueContractGroupingHierarchyRecord(
                id,
                sequence,
                revenueContractGroupingTemplateId,
                groupingFields,
                createdPeriodId,
                isActive,
                createdAt,
                updatedAt
        );
    }
}
