package com.revrec.engine.common.service.RevenueContractHeader;

import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractHeaderService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractHeaderRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractHeaderService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractHeaderRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `RevenueContractId`, `version`, `TotalSellPrice`, `TotalListPrice`, `TotalCarveAmount`, `createdPeriodId`, `InitialContractModificationDate`, `ContractModificationDate`, `isRevenueContractPosted`, `AllocationTreatment`, `CreatedBy`, `CreatedAt`, `UpdatedBy`, `UpdatedAt`, `IsAllocationInitialEntryCreated`, `IsActive` FROM `revenueContractHeader`";

    private static final String NEXT_REVENUE_CONTRACT_ID = "SELECT NEXTVAL(revenueContractHeaderIdSeq)";
    
    public Optional<RevenueContractHeaderRecord> findById(Long revenueContractId) {
        var list = jdbc.query(SELECT + " WHERE `RevenueContractId` = :id", Map.of("id", revenueContractId), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractHeaderRecord> findByIdCached(Long revenueContractId) {
        return noSqlRecordServer
                .get(RevenueContractHeaderRecord.TABLE_NAME, String.valueOf(revenueContractId), RevenueContractHeaderRecord.class)
                .or(() -> findById(revenueContractId).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractHeaderRecord.TABLE_NAME, String.valueOf(row.revenueContractId()), row);
                    return row;
                }));
    }
    public List<RevenueContractHeaderRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }

    /**
     * Returns the next {@code RevenueContractId} from {@code revenueContractHeaderIdSeq}
     * (see {@code ProjectDetails/db_script_sequence.sql}).
     */
    public long nextRevenueContractId() {
        Long id = jdbc.queryForObject(NEXT_REVENUE_CONTRACT_ID, Map.of(), Long.class);
        if (id == null) {
            throw new IllegalStateException("NEXTVAL(revenueContractHeaderIdSeq) returned null");
        }
        return id;
    }
}
