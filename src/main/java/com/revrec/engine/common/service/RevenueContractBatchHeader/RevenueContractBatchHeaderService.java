package com.revrec.engine.common.service.RevenueContractBatchHeader;

import com.revrec.engine.domain.service.RevenueContractBatchHeader.RevenueContractBatchHeaderRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractBatchHeader.RevenueContractBatchHeaderRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractBatchHeaderService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractBatchHeaderRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractBatchHeaderService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractBatchHeaderRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `BatchId`, `Name`, `Description`, `RevenueContractGroupingTemplateId`, `Status`, `StageRecordProcessed`, `revenueContractRecordProcessed`, `createdPeriodId`, `CreatedBy`, `CreatedAt`, `UpdatedBy`, `UpdatedAt` FROM `revenueContractBatchHeader`";
    public Optional<RevenueContractBatchHeaderRecord> findById(Long batchId) {
        var list = jdbc.query(SELECT + " WHERE `BatchId` = :id", Map.of("id", batchId), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractBatchHeaderRecord> findByIdCached(Long batchId) {
        return noSqlRecordServer
                .get(RevenueContractBatchHeaderRecord.TABLE_NAME, String.valueOf(batchId), RevenueContractBatchHeaderRecord.class)
                .or(() -> findById(batchId).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractBatchHeaderRecord.TABLE_NAME, String.valueOf(row.batchId()), row);
                    return row;
                }));
    }
    public List<RevenueContractBatchHeaderRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
