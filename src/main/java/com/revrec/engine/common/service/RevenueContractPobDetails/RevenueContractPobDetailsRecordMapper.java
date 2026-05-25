package com.revrec.engine.domain.service.RevenueContractPobDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractPobDetailsRecord}.
 */
@Component
public final class RevenueContractPobDetailsRecordMapper implements RowMapper<RevenueContractPobDetailsRecord> {

    @Override
    public @NonNull RevenueContractPobDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var pobTemplateId = rs.getObject(c++, Long.class);
        var pobRuleId = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var pobExpiryDate = rs.getObject(c++, java.time.LocalDate.class);
        var processsedFlag = rs.getString(c++);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new RevenueContractPobDetailsRecord(
                id,
                revenueContractId,
                pobTemplateId,
                pobRuleId,
                name,
                description,
                pobExpiryDate,
                processsedFlag,
                isActive,
                createdPeriodId,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy
        );
    }
}
