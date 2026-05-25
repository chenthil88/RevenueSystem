package com.revrec.engine.domain.service.RevenueContractModificationDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractModificationDetailsRecord}.
 * Column order matches {@code db_script.sql} ({@code RevenueContractModificationDetails}).
 */
@Component
public final class RevenueContractModificationDetailsRecordMapper
        implements RowMapper<RevenueContractModificationDetailsRecord> {

    @Override
    public @NonNull RevenueContractModificationDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum)
            throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var revenueContractLineId = rs.getObject(c++, Long.class);
        var contractModificationRuleName = rs.getString(c++);
        var contractModificationDate = rs.getObject(c++, java.time.LocalDate.class);
        var contractModificationRuleTreatment = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new RevenueContractModificationDetailsRecord(
                id,
                revenueContractId,
                revenueContractLineId,
                contractModificationRuleName,
                contractModificationDate,
                contractModificationRuleTreatment,
                createdPeriodId,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy
        );
    }
}
