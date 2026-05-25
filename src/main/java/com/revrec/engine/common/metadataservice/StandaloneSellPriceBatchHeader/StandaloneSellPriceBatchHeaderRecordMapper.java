package com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchHeader;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link StandaloneSellPriceBatchHeaderRecord}.
 */
@Component
public final class StandaloneSellPriceBatchHeaderRecordMapper implements RowMapper<StandaloneSellPriceBatchHeaderRecord> {

    @Override
    public @NonNull StandaloneSellPriceBatchHeaderRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var standaloneSellPriceTemplateId = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var sspType = rs.getString(c++);
        var effectiveFromDate = rs.getObject(c++, java.time.LocalDate.class);
        var effectiveToDate = rs.getObject(c++, java.time.LocalDate.class);
        var status = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdBy = rs.getString(c++);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedBy = rs.getString(c++);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var isActive = rs.getObject(c++, Boolean.class);
        return new StandaloneSellPriceBatchHeaderRecord(
                id,
                standaloneSellPriceTemplateId,
                name,
                description,
                sspType,
                effectiveFromDate,
                effectiveToDate,
                status,
                createdPeriodId,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt,
                isActive
        );
    }
}
