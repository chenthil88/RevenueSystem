package com.revrec.engine.domain.metadataservice.StandaloneSellPriceHierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link StandaloneSellPriceHierarchyRecord}.
 */
@Component
public final class StandaloneSellPriceHierarchyRecordMapper implements RowMapper<StandaloneSellPriceHierarchyRecord> {

    @Override
    public @NonNull StandaloneSellPriceHierarchyRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var seq = rs.getObject(c++, Long.class);
        var standaloneSellPriceTemplateId = rs.getObject(c++, Long.class);
        var standaloneSellPriceTemplateName = rs.getString(c++);
        var revenueContractGroupingTemplateId = rs.getObject(c++, Long.class);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new StandaloneSellPriceHierarchyRecord(
                seq,
                standaloneSellPriceTemplateId,
                standaloneSellPriceTemplateName,
                revenueContractGroupingTemplateId,
                isActive,
                createdPeriodId,
                createdAt,
                updatedAt
        );
    }
}
