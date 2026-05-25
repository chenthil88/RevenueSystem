package com.revrec.engine.common.metadataservice.Currency;

import com.revrec.engine.domain.metadataservice.Currency.CurrencyRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.Currency.CurrencyRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class CurrencyService {

    private final NamedParameterJdbcTemplate jdbc;
    private final CurrencyRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public CurrencyService(
            NamedParameterJdbcTemplate jdbc,
            CurrencyRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `CurrencyName`, `CurrencyCode`, `CurrencyRounding`, `CreatedAt`, `UpdatedAt` FROM `Currency`";
    public Optional<CurrencyRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<CurrencyRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(CurrencyRecord.TABLE_NAME, String.valueOf(id), CurrencyRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            CurrencyRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<CurrencyRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
