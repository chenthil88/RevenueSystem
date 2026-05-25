package com.revrec.engine.common.metadataservice.StandaloneSellPriceBatchDetails;

import com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchDetails.StandaloneSellPriceBatchDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchDetails.StandaloneSellPriceBatchDetailsRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class StandaloneSellPriceBatchDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final StandaloneSellPriceBatchDetailsRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public StandaloneSellPriceBatchDetailsService(
            NamedParameterJdbcTemplate jdbc,
            StandaloneSellPriceBatchDetailsRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `BatchId`, `StandaloneSellPriceTemplateId`, `attributeField1`, `attributeField2`, `attributeField3`, `attributeField4`, `attributeField5`, `attributeField6`, `attributeField7`, `attributeField8`, `attributeField9`, `attributeField10`, `aboveSspPercentage`, `sspPercentage`, `belowSspPercentage`, `aboveSspPrice`, `sspPrice`, `belowSspPrice`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy` FROM `StandaloneSellPriceBatchDetails`";
    public Optional<StandaloneSellPriceBatchDetailsRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<StandaloneSellPriceBatchDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(StandaloneSellPriceBatchDetailsRecord.TABLE_NAME, String.valueOf(id), StandaloneSellPriceBatchDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            StandaloneSellPriceBatchDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<StandaloneSellPriceBatchDetailsRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
