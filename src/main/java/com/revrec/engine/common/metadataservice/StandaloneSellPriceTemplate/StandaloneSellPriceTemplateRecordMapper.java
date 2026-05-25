package com.revrec.engine.domain.metadataservice.StandaloneSellPriceTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link StandaloneSellPriceTemplateRecord}.
 */
@Component
public final class StandaloneSellPriceTemplateRecordMapper implements RowMapper<StandaloneSellPriceTemplateRecord> {

    @Override
    public @NonNull StandaloneSellPriceTemplateRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var stratificationFields = rs.getString(c++);
        var applyFieldName = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new StandaloneSellPriceTemplateRecord(
                id,
                name,
                description,
                stratificationFields,
                applyFieldName,
                createdPeriodId,
                isActive,
                createdAt,
                updatedAt
        );
    }
}
