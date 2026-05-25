package com.revrec.engine.common.service.RevenueContractBatchDetails;

import com.revrec.engine.domain.service.RevenueContractBatchDetails.RevenueContractBatchDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractBatchDetails.RevenueContractBatchDetailsRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractBatchDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractBatchDetailsRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractBatchDetailsService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractBatchDetailsRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `BatchId`, `RevenueContractId`, `StageRecordProcessed`, `revenueContractRecordProcessed`, `processedFlag`, `createdPeriodId`, `CreatedBy`, `CreatedAt`, `UpdatedBy`, `UpdatedAt` FROM `revenueContractBatchDetails`";
    public Optional<RevenueContractBatchDetailsRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractBatchDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractBatchDetailsRecord.TABLE_NAME, String.valueOf(id), RevenueContractBatchDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractBatchDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractBatchDetailsRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
