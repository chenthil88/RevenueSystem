package com.revrec.engine.common.service.RevenueContractPobDetails;

import com.revrec.engine.domain.service.RevenueContractPobDetails.RevenueContractPobDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractPobDetails.RevenueContractPobDetailsRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractPobDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractPobDetailsRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractPobDetailsService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractPobDetailsRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `revenueContractId`, `pobTemplateId`, `pobRuleId`, `Name`, `Description`, `pobExpiryDate`, `processsedFlag`, `IsActive`, `createdPeriodId`, `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy` FROM `revenueContractPobDetails`";
    public Optional<RevenueContractPobDetailsRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractPobDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractPobDetailsRecord.TABLE_NAME, String.valueOf(id), RevenueContractPobDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractPobDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractPobDetailsRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
