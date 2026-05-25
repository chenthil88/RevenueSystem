package com.revrec.engine.domain.metadataservice.PerformanceObligationRule;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link PerformanceObligationRuleRecord}.
 */
@Component
public final class PerformanceObligationRuleRecordMapper implements RowMapper<PerformanceObligationRuleRecord> {

    @Override
    public @NonNull PerformanceObligationRuleRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var performanceObligationTemplateId = rs.getObject(c++, Long.class);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new PerformanceObligationRuleRecord(
                id,
                name,
                description,
                performanceObligationTemplateId,
                isActive,
                createdPeriodId,
                createdAt,
                updatedAt
        );
    }
}
