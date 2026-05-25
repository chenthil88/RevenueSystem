package com.revrec.engine.common.metadataservice.RevenueContractGroupingFilter;

import com.revrec.engine.domain.metadataservice.RevenueContractGroupingFilter.RevenueContractGroupingFilterRecord;
import com.revrec.engine.domain.metadataservice.RevenueContractGroupingFilter.RevenueContractGroupingFilterRecordMapper;
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
public class RevenueContractGroupingFilterService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractGroupingFilterRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractGroupingFilterService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractGroupingFilterRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `RevenueContractGroupingTemplateId`, `FilterFieldName`, `FilterOperator`, `FilterValue`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy` FROM `RevenueContractGroupingFilter`";
    public Optional<RevenueContractGroupingFilterRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `Id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractGroupingFilterRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractGroupingFilterRecord.TABLE_NAME, String.valueOf(id), RevenueContractGroupingFilterRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractGroupingFilterRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractGroupingFilterRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
