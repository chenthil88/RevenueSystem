package com.revrec.engine.domain.metadataservice.PerformanceObligationRuleFilter;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link PerformanceObligationRuleFilterRecord}.
 */
@Component
public final class PerformanceObligationRuleFilterRecordMapper implements RowMapper<PerformanceObligationRuleFilterRecord> {

    @Override
    public @NonNull PerformanceObligationRuleFilterRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var performanceObligationRuleId = rs.getObject(c++, Long.class);
        var filterFieldName = rs.getString(c++);
        var filterOperator = rs.getString(c++);
        var filterValue = rs.getString(c++);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new PerformanceObligationRuleFilterRecord(
                id,
                performanceObligationRuleId,
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
