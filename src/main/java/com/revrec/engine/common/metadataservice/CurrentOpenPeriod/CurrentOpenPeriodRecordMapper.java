package com.revrec.engine.domain.metadataservice.CurrentOpenPeriod;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link CurrentOpenPeriodRecord}.
 */
@Component
public final class CurrentOpenPeriodRecordMapper implements RowMapper<CurrentOpenPeriodRecord> {

    @Override
    public @NonNull CurrentOpenPeriodRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var openPeriodId = rs.getObject(c++, Long.class);
        var openPeriodName = rs.getString(c++);
        var openPeriodStartDate = rs.getObject(c++, java.time.LocalDate.class);
        var openPeriodEndDate = rs.getObject(c++, java.time.LocalDate.class);
        var openPeriodStatus = rs.getString(c++);
        var openPeriodCreatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var openPeriodUpdatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var organizationId = rs.getObject(c++, Long.class);
        var bookId = rs.getObject(c++, Long.class);
        return new CurrentOpenPeriodRecord(
                openPeriodId,
                openPeriodName,
                openPeriodStartDate,
                openPeriodEndDate,
                openPeriodStatus,
                openPeriodCreatedAt,
                openPeriodUpdatedAt,
                organizationId,
                bookId
        );
    }
}
