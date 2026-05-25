package com.revrec.engine.domain.metadataservice.PerformanceObligationTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link PerformanceObligationTemplateRecord}.
 */
@Component
public final class PerformanceObligationTemplateRecordMapper implements RowMapper<PerformanceObligationTemplateRecord> {

    @Override
    public @NonNull PerformanceObligationTemplateRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var revenueReleaseMethod = rs.getString(c++);
        var revenueReleaseTiming = rs.getString(c++);
        var revenueCalculationMethod = rs.getString(c++);
        var isDistinctPob = rs.getObject(c++, Boolean.class);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new PerformanceObligationTemplateRecord(
                id,
                name,
                description,
                revenueReleaseMethod,
                revenueReleaseTiming,
                revenueCalculationMethod,
                isDistinctPob,
                isActive,
                createdPeriodId,
                createdAt,
                updatedAt
        );
    }
}
