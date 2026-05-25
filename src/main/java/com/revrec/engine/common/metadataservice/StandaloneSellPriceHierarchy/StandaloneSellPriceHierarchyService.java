package com.revrec.engine.common.metadataservice.StandaloneSellPriceHierarchy;

import com.revrec.engine.domain.metadataservice.StandaloneSellPriceHierarchy.StandaloneSellPriceHierarchyRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.StandaloneSellPriceHierarchy.StandaloneSellPriceHierarchyRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class StandaloneSellPriceHierarchyService {

    private final NamedParameterJdbcTemplate jdbc;
    private final StandaloneSellPriceHierarchyRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public StandaloneSellPriceHierarchyService(
            NamedParameterJdbcTemplate jdbc,
            StandaloneSellPriceHierarchyRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `seq`, `StandaloneSellPriceTemplateId`, `StandaloneSellPriceTemplateName`, `RevenueContractGroupingTemplateId`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt` FROM `StandaloneSellPriceHierarchy`";
    public Optional<StandaloneSellPriceHierarchyRecord> findById(Long seq) {
        var list = jdbc.query(SELECT + " WHERE `seq` = :id", Map.of("id", seq), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<StandaloneSellPriceHierarchyRecord> findByIdCached(Long seq) {
        return noSqlRecordServer
                .get(StandaloneSellPriceHierarchyRecord.TABLE_NAME, String.valueOf(seq), StandaloneSellPriceHierarchyRecord.class)
                .or(() -> findById(seq).map(row -> {
                    noSqlRecordServer.put(
                            StandaloneSellPriceHierarchyRecord.TABLE_NAME, String.valueOf(row.seq()), row);
                    return row;
                }));
    }
    public List<StandaloneSellPriceHierarchyRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
