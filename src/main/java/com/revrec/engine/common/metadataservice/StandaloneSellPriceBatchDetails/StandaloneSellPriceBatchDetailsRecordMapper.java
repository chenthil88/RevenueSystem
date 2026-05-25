package com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link StandaloneSellPriceBatchDetailsRecord}.
 */
@Component
public final class StandaloneSellPriceBatchDetailsRecordMapper implements RowMapper<StandaloneSellPriceBatchDetailsRecord> {

    @Override
    public @NonNull StandaloneSellPriceBatchDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var batchId = rs.getObject(c++, Long.class);
        var standaloneSellPriceTemplateId = rs.getObject(c++, Long.class);
        var attributeField1 = rs.getString(c++);
        var attributeField2 = rs.getString(c++);
        var attributeField3 = rs.getString(c++);
        var attributeField4 = rs.getString(c++);
        var attributeField5 = rs.getString(c++);
        var attributeField6 = rs.getString(c++);
        var attributeField7 = rs.getString(c++);
        var attributeField8 = rs.getString(c++);
        var attributeField9 = rs.getString(c++);
        var attributeField10 = rs.getString(c++);
        var aboveSspPercentage = rs.getBigDecimal(c++);
        var sspPercentage = rs.getBigDecimal(c++);
        var belowSspPercentage = rs.getBigDecimal(c++);
        var aboveSspPrice = rs.getBigDecimal(c++);
        var sspPrice = rs.getBigDecimal(c++);
        var belowSspPrice = rs.getBigDecimal(c++);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new StandaloneSellPriceBatchDetailsRecord(
                id,
                batchId,
                standaloneSellPriceTemplateId,
                attributeField1,
                attributeField2,
                attributeField3,
                attributeField4,
                attributeField5,
                attributeField6,
                attributeField7,
                attributeField8,
                attributeField9,
                attributeField10,
                aboveSspPercentage,
                sspPercentage,
                belowSspPercentage,
                aboveSspPrice,
                sspPrice,
                belowSspPrice,
                isActive,
                createdPeriodId,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy
        );
    }
}
