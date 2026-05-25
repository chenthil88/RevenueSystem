package com.revrec.engine.common.metadataservice.RevenueContractGroupingTemplate;

import com.revrec.engine.domain.metadataservice.RevenueContractGroupingTemplate.RevenueContractGroupingTemplateRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.RevenueContractGroupingTemplate.RevenueContractGroupingTemplateRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractGroupingTemplateService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractGroupingTemplateRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractGroupingTemplateService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractGroupingTemplateRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `Name`, `Description`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt` FROM `RevenueContractGroupingTemplate`";
    public Optional<RevenueContractGroupingTemplateRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }

    public Optional<RevenueContractGroupingTemplateRecord> findByName(String name) {
        var list = jdbc.query(
                SELECT + " WHERE `Name` = :name",
                Map.of("name", name),
                rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractGroupingTemplateRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractGroupingTemplateRecord.TABLE_NAME, String.valueOf(id), RevenueContractGroupingTemplateRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractGroupingTemplateRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractGroupingTemplateRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
