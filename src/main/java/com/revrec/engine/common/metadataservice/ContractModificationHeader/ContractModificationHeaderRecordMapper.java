package com.revrec.engine.domain.metadataservice.ContractModificationHeader;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link ContractModificationHeaderRecord}.
 */
@Component
public final class ContractModificationHeaderRecordMapper implements RowMapper<ContractModificationHeaderRecord> {

    @Override
    public @NonNull ContractModificationHeaderRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Integer.class);
        var initialContractModificationDuration = rs.getString(c++);
        var revisionContractModificationDuration = rs.getString(c++);
        var initialContractModificationSspDateMethod = rs.getString(c++);
        var revisionContractModificationSspDateMethod = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Integer.class);
        var updatedPeriodId = rs.getObject(c++, Integer.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new ContractModificationHeaderRecord(
                id,
                initialContractModificationDuration,
                revisionContractModificationDuration,
                initialContractModificationSspDateMethod,
                revisionContractModificationSspDateMethod,
                createdPeriodId,
                updatedPeriodId,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy
        );
    }
}
