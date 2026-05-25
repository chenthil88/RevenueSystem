package com.revrec.engine.common.service.RevenueContractBillingAccountDetails;

import com.revrec.engine.domain.service.RevenueContractBillingAccountDetails.RevenueContractBillingAccountDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractBillingAccountDetails.RevenueContractBillingAccountDetailsRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractBillingAccountDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractBillingAccountDetailsRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractBillingAccountDetailsService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractBillingAccountDetailsRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `revenueContractId`, `revenueContractOrderDetailsId`, `DeferSegment1`, `DeferSegment2`, `DeferSegment3`, `DeferSegment4`, `DeferSegment5`, `DeferSegment6`, `DeferSegment7`, `DeferSegment8`, `DeferSegment9`, `DeferSegment10`, `RevenueSegment1`, `RevenueSegment2`, `RevenueSegment3`, `RevenueSegment4`, `RevenueSegment5`, `RevenueSegment6`, `RevenueSegment7`, `RevenueSegment8`, `RevenueSegment9`, `RevenueSegment10`, `customSegment1`, `customSegment2`, `customSegment3`, `customSegment4`, `customSegment5`, `customSegment6`, `customSegment7`, `customSegment8`, `customSegment9`, `customSegment10`, `createdPeriodId`, `updatedPeriodId`, `createdAt`, `updatedAt`, `CreatedBy`, `UpdatedBy` FROM `revenueContractBillingAccountDetails`";
    public Optional<RevenueContractBillingAccountDetailsRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractBillingAccountDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractBillingAccountDetailsRecord.TABLE_NAME, String.valueOf(id), RevenueContractBillingAccountDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractBillingAccountDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractBillingAccountDetailsRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
