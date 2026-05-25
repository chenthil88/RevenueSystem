package com.revrec.engine.domain.metadataservice.RevenueContractGroupingFilter;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractGroupingFilterRecord}.
 */
@Component
public final class RevenueContractGroupingFilterRecordMapper implements RowMapper<RevenueContractGroupingFilterRecord> {

    @Override
    public @NonNull RevenueContractGroupingFilterRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractGroupingTemplateId = rs.getObject(c++, Long.class);
        var filterFieldName = rs.getString(c++);
        var filterOperator = rs.getString(c++);
        var filterValue = rs.getString(c++);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new RevenueContractGroupingFilterRecord(
                id,
                revenueContractGroupingTemplateId,
                filterFieldName,
                filterOperator,
                filterValue,
                isActive,
                createdPeriodId,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy
        );
    }
}
