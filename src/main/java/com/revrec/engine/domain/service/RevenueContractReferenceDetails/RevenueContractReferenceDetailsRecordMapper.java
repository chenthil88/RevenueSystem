package com.revrec.engine.domain.service.RevenueContractReferenceDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public final class RevenueContractReferenceDetailsRecordMapper
        implements RowMapper<RevenueContractReferenceDetailsRecord> {

    @Override
    public @NonNull RevenueContractReferenceDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum)
            throws SQLException {
        int c = 1;
        return new RevenueContractReferenceDetailsRecord(
                rs.getObject(c++, Long.class),
                rs.getObject(c++, Long.class),
                rs.getString(c++),
                rs.getString(c++),
                rs.getObject(c++, Long.class),
                rs.getObject(c++, Boolean.class),
                rs.getObject(c++, java.time.LocalDateTime.class),
                rs.getObject(c++, java.time.LocalDateTime.class));
    }
}
