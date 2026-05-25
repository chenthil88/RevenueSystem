package com.revrec.engine.domain.metadataservice.Calendar;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link CalendarRecord}.
 */
@Component
public final class CalendarRecordMapper implements RowMapper<CalendarRecord> {

    @Override
    public @NonNull CalendarRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var periodId = rs.getObject(c++, Long.class);
        var periodName = rs.getString(c++);
        var quarterStartDate = rs.getObject(c++, java.time.LocalDate.class);
        var quarterEndDate = rs.getObject(c++, java.time.LocalDate.class);
        var monthStartDate = rs.getObject(c++, java.time.LocalDate.class);
        var monthEndDate = rs.getObject(c++, java.time.LocalDate.class);
        var yearStartDate = rs.getObject(c++, java.time.LocalDate.class);
        var yearEndDate = rs.getObject(c++, java.time.LocalDate.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new CalendarRecord(
                periodId,
                periodName,
                quarterStartDate,
                quarterEndDate,
                monthStartDate,
                monthEndDate,
                yearStartDate,
                yearEndDate,
                createdAt,
                updatedAt
        );
    }
}
