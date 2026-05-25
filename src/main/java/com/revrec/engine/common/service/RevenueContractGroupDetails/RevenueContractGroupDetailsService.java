package com.revrec.engine.common.service.RevenueContractGroupDetails;

import com.revrec.engine.domain.service.RevenueContractGroupDetails.RevenueContractGroupDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.query.repository.RevenueContractGroupDetailsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 * Data access is routed through the multi-database query engine.
 */
@Service
public class RevenueContractGroupDetailsService {

    private static final String CLOSED_FLAG_OPEN = "N";

    private static final String NEXT_ID = "SELECT NEXTVAL(revenue_contract_group_details_id_seq)";

    private static final String INSERT =
            """
            INSERT INTO `revenueContractGroupDetails`
                (`Id`, `RevenueContractId`, `GroupingValue`, `ClosedFlag`, `createdPeriodId`, `IsActive`, `CreatedAt`, `UpdatedAt`)
            VALUES
                (:id, :revenueContractId, :groupingValue, :closedFlag, :createdPeriodId, :isActive, :createdAt, :updatedAt)
            """;

    private final RevenueContractGroupDetailsRepository repository;
    private final NamedParameterJdbcTemplate jdbc;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractGroupDetailsService(
            RevenueContractGroupDetailsRepository repository,
            NamedParameterJdbcTemplate jdbc,
            NoSqlRecordServer noSqlRecordServer) {
        this.repository = repository;
        this.jdbc = jdbc;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    public long nextId() {
        Long id = jdbc.queryForObject(NEXT_ID, Map.of(), Long.class);
        if (id == null) {
            throw new IllegalStateException("NEXTVAL(revenue_contract_group_details_id_seq) returned null");
        }
        return id;
    }

    public void insertAll(List<RevenueContractGroupDetailsRecord> records) {
        if (records.isEmpty()) {
            return;
        }
        SqlParameterSource[] batch = records.stream()
                .map(record -> new MapSqlParameterSource()
                        .addValue("id", record.id())
                        .addValue("revenueContractId", record.revenueContractId())
                        .addValue("groupingValue", record.groupingValue())
                        .addValue("closedFlag", record.closedFlag())
                        .addValue("createdPeriodId", record.createdPeriodId())
                        .addValue("isActive", record.isActive())
                        .addValue("createdAt", record.createdAt())
                        .addValue("updatedAt", record.updatedAt()))
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(INSERT, batch);
    }

    /**
     * Adds one group-detail row the first time {@code groupingValue} is seen in a batch assignment pass.
     */
    public void captureGroupDetail(
            String groupingValue,
            long revenueContractId,
            Long createdPeriodId,
            LocalDateTime timestamp,
            Set<String> registeredGroupValues,
            List<RevenueContractGroupDetailsRecord> target) {
        if (!registeredGroupValues.add(groupingValue)) {
            return;
        }
        target.add(new RevenueContractGroupDetailsRecord(
                nextId(),
                revenueContractId,
                groupingValue,
                CLOSED_FLAG_OPEN,
                createdPeriodId,
                true,
                timestamp,
                timestamp));
    }

    public Optional<RevenueContractGroupDetailsRecord> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<RevenueContractGroupDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractGroupDetailsRecord.TABLE_NAME, String.valueOf(id), RevenueContractGroupDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractGroupDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }

    public List<RevenueContractGroupDetailsRecord> findByRevenueContractId(Long revenueContractId) {
        return repository.findByRevenueContractId(revenueContractId);
    }

    public List<RevenueContractGroupDetailsRecord> findAll(int limit, int offset) {
        return repository.findAll(limit, offset);
    }
}
