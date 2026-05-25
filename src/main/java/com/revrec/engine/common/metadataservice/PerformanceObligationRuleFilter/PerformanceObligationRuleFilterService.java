package com.revrec.engine.common.metadataservice.PerformanceObligationRuleFilter;

import com.revrec.engine.domain.metadataservice.PerformanceObligationRuleFilter.PerformanceObligationRuleFilterRecord;
import com.revrec.engine.domain.metadataservice.PerformanceObligationRuleFilter.PerformanceObligationRuleFilterRecordMapper;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class PerformanceObligationRuleFilterService {

    private final NamedParameterJdbcTemplate jdbc;
    private final PerformanceObligationRuleFilterRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public PerformanceObligationRuleFilterService(
            NamedParameterJdbcTemplate jdbc,
            PerformanceObligationRuleFilterRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `PerformanceObligationRuleId`, `FilterFieldName`, `FilterOperator`, `FilterValue`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy` FROM `PerformanceObligationRuleFilter`";
    public Optional<PerformanceObligationRuleFilterRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<PerformanceObligationRuleFilterRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(PerformanceObligationRuleFilterRecord.TABLE_NAME, String.valueOf(id), PerformanceObligationRuleFilterRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            PerformanceObligationRuleFilterRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<PerformanceObligationRuleFilterRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
