package com.revrec.engine.common.metadataservice.StandaloneSellPriceTemplate;

import com.revrec.engine.domain.metadataservice.StandaloneSellPriceTemplate.StandaloneSellPriceTemplateRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.StandaloneSellPriceTemplate.StandaloneSellPriceTemplateRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class StandaloneSellPriceTemplateService {

    private final NamedParameterJdbcTemplate jdbc;
    private final StandaloneSellPriceTemplateRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public StandaloneSellPriceTemplateService(
            NamedParameterJdbcTemplate jdbc,
            StandaloneSellPriceTemplateRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `Name`, `Description`, `stratificationFields`, `ApplyFieldName`, `createdPeriodId`, `IsActive`, `CreatedAt`, `UpdatedAt` FROM `StandaloneSellPriceTemplate`";
    public Optional<StandaloneSellPriceTemplateRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<StandaloneSellPriceTemplateRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(StandaloneSellPriceTemplateRecord.TABLE_NAME, String.valueOf(id), StandaloneSellPriceTemplateRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            StandaloneSellPriceTemplateRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<StandaloneSellPriceTemplateRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
