package com.revrec.engine.domain.metadataservice.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link CurrencyRecord}.
 */
@Component
public final class CurrencyRecordMapper implements RowMapper<CurrencyRecord> {

    @Override
    public @NonNull CurrencyRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var currencyName = rs.getString(c++);
        var currencyCode = rs.getString(c++);
        var currencyRounding = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        return new CurrencyRecord(
                id,
                currencyName,
                currencyCode,
                currencyRounding,
                createdAt,
                updatedAt
        );
    }
}
