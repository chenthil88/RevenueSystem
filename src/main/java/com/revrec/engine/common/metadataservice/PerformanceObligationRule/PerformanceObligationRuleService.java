package com.revrec.engine.common.metadataservice.PerformanceObligationRule;

import com.revrec.engine.domain.metadataservice.PerformanceObligationRule.PerformanceObligationRuleRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.PerformanceObligationRule.PerformanceObligationRuleRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class PerformanceObligationRuleService {

    private final NamedParameterJdbcTemplate jdbc;
    private final PerformanceObligationRuleRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public PerformanceObligationRuleService(
            NamedParameterJdbcTemplate jdbc,
            PerformanceObligationRuleRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `Name`, `Description`, `PerformanceObligationTemplateId`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt` FROM `PerformanceObligationRule`";
    public Optional<PerformanceObligationRuleRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<PerformanceObligationRuleRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(PerformanceObligationRuleRecord.TABLE_NAME, String.valueOf(id), PerformanceObligationRuleRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            PerformanceObligationRuleRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<PerformanceObligationRuleRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
