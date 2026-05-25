package com.revrec.engine.common.metadataservice.PerformanceObligationTemplate;

import com.revrec.engine.domain.metadataservice.PerformanceObligationTemplate.PerformanceObligationTemplateRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.PerformanceObligationTemplate.PerformanceObligationTemplateRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class PerformanceObligationTemplateService {

    private final NamedParameterJdbcTemplate jdbc;
    private final PerformanceObligationTemplateRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public PerformanceObligationTemplateService(
            NamedParameterJdbcTemplate jdbc,
            PerformanceObligationTemplateRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `Name`, `Description`, `RevenueReleaseMethod`, `RevenueReleaseTiming`, `RevenueCalculationMethod`, `IsDistinctPob`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt` FROM `PerformanceObligationTemplate`";
    public Optional<PerformanceObligationTemplateRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<PerformanceObligationTemplateRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(PerformanceObligationTemplateRecord.TABLE_NAME, String.valueOf(id), PerformanceObligationTemplateRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            PerformanceObligationTemplateRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<PerformanceObligationTemplateRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
