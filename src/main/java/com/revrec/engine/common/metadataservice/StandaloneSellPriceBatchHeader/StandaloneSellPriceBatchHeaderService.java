package com.revrec.engine.common.metadataservice.StandaloneSellPriceBatchHeader;

import com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchHeader.StandaloneSellPriceBatchHeaderRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchHeader.StandaloneSellPriceBatchHeaderRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class StandaloneSellPriceBatchHeaderService {

    private final NamedParameterJdbcTemplate jdbc;
    private final StandaloneSellPriceBatchHeaderRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public StandaloneSellPriceBatchHeaderService(
            NamedParameterJdbcTemplate jdbc,
            StandaloneSellPriceBatchHeaderRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `StandaloneSellPriceTemplateId`, `Name`, `Description`, `SspType`, `EffectiveFromDate`, `EffectiveToDate`, `Status`, `createdPeriodId`, `CreatedBy`, `CreatedAt`, `UpdatedBy`, `UpdatedAt`, `IsActive` FROM `StandaloneSellPriceBatchHeader`";
    public Optional<StandaloneSellPriceBatchHeaderRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<StandaloneSellPriceBatchHeaderRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(StandaloneSellPriceBatchHeaderRecord.TABLE_NAME, String.valueOf(id), StandaloneSellPriceBatchHeaderRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            StandaloneSellPriceBatchHeaderRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<StandaloneSellPriceBatchHeaderRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
