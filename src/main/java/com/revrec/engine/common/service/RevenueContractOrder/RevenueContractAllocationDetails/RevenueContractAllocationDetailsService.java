package com.revrec.engine.common.service.RevenueContractOrder.RevenueContractAllocationDetails;

import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecordMapper;
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
public class RevenueContractAllocationDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractAllocationDetailsRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractAllocationDetailsService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractAllocationDetailsRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `revenueContractId`, `ExtendedSspPrice`, `allocationCurrency`, `exchangeRate`, `globalexchangeRate`, `exchangeRateDate`, `CarveAmount`, `UnreleasedCarveAmount`, `CumulativeReleasedAmount`, `CumulativeUnReleasedAmount`, `TransactionPrice`, `AllocatedPrice`, `NetQuantity`, `Term`, `BookId`, `OrganizationId`, `TransactionFunctionalPrice`, `SspTemplateId`, `SspId`, `sspType`, `sspPrice`, `sspPercentage`, `aboveSspPrice`, `belowSspPrice`, `belowMidPercentage`, `aboveMidPercentage`, `IsCancelOrder`, `IsReturnOrder`, `createdPeriodId`, `CumulativeCarveAmount`, `CumulativeAllocatedPrice`, `Comments`, `CreatedBy`, `CreatedAt`, `UpdatedBy`, `UpdatedAt` FROM `revenueContractAllocationDetails`";
    public Optional<RevenueContractAllocationDetailsRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractAllocationDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractAllocationDetailsRecord.TABLE_NAME, String.valueOf(id), RevenueContractAllocationDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractAllocationDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractAllocationDetailsRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
