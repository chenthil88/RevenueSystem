package com.revrec.engine.domain.metadataservice.ContractModificationDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link ContractModificationDetailsRecord} by column label (order-independent).
 */
@Component
public final class ContractModificationDetailsRecordMapper implements RowMapper<ContractModificationDetailsRecord> {

    @Override
    public @NonNull ContractModificationDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        var id = rs.getObject("id", Integer.class);
        var ruleName = rs.getString("RuleName");
        var ruleTreatmentForDistinctPob = rs.getString("RuleTreatmentForDistinctPob");
        var ruleTreatmentForNonDistinctPob = rs.getString("RuleTreatmentForNonDistinctPob");
        var ruleCategory = rs.getString("ruleCategory");
        var isActive = rs.getObject("IsActive", Boolean.class);
        var createdPeriodId = rs.getObject("createdPeriodId", Integer.class);
        var updatedPeriodId = rs.getObject("updatedPeriodId", Integer.class);
        var createdAt = rs.getObject("createdAt", java.time.LocalDateTime.class);
        var updatedAt = rs.getObject("updatedAt", java.time.LocalDateTime.class);
        var createdBy = rs.getString("createdBy");
        return new ContractModificationDetailsRecord(
                id,
                ruleName,
                ruleTreatmentForDistinctPob,
                ruleTreatmentForNonDistinctPob,
                ruleCategory,
                isActive,
                createdPeriodId,
                updatedPeriodId,
                createdAt,
                updatedAt,
                createdBy
        );
    }
}
