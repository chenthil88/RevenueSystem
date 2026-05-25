package com.revrec.engine.domain.metadataservice.JournalAccountsSetup;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link JournalAccountsSetupRecord}.
 */
@Component
public final class JournalAccountsSetupRecordMapper implements RowMapper<JournalAccountsSetupRecord> {

    @Override
    public @NonNull JournalAccountsSetupRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var name = rs.getString(c++);
        var description = rs.getString(c++);
        var segmentPosition1 = rs.getString(c++);
        var segmentPosition2 = rs.getString(c++);
        var segmentPosition3 = rs.getString(c++);
        var segmentPosition4 = rs.getString(c++);
        var segmentPosition5 = rs.getString(c++);
        var segmentPosition6 = rs.getString(c++);
        var segmentPosition7 = rs.getString(c++);
        var segmentPosition8 = rs.getString(c++);
        var segmentPosition9 = rs.getString(c++);
        var segmentPosition10 = rs.getString(c++);
        var isActive = rs.getObject(c++, Boolean.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new JournalAccountsSetupRecord(
                id,
                name,
                description,
                segmentPosition1,
                segmentPosition2,
                segmentPosition3,
                segmentPosition4,
                segmentPosition5,
                segmentPosition6,
                segmentPosition7,
                segmentPosition8,
                segmentPosition9,
                segmentPosition10,
                isActive,
                createdAt,
                updatedAt
        );
    }
}
