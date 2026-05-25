package com.revrec.engine.domain.metadataservice.RevenueContractGroupingTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractGroupingTemplateRecord}.
 */
@Component
public final class RevenueContractGroupingTemplateRecordMapper implements RowMapper<RevenueContractGroupingTemplateRecord> {

    @Override
    public @NonNull RevenueContractGroupingTemplateRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new RevenueContractGroupingTemplateRecord(
                id,
                name,
                description,
                isActive,
                createdPeriodId,
                createdAt,
                updatedAt
        );
    }
}
