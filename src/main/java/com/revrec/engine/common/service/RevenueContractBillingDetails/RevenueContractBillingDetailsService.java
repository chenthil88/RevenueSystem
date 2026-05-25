package com.revrec.engine.common.service.RevenueContractBillingDetails;

import com.revrec.engine.domain.service.RevenueContractBillingDetails.RevenueContractBillingDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractBillingDetails.RevenueContractBillingDetailsRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractBillingDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractBillingDetailsRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractBillingDetailsService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractBillingDetailsRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `revenueContractOrderDetailsId`, `transactionType`, `revenueContractId`, `InvoiceId`, `InvoiceNumber`, `InvoiceDate`, `SellPrice`, `ListPrice`, `UnitSellPrice`, `UnitListPrice`, `transactionCurrency`, `FunctionalCurrency`, `exchangeRate`, `globalexchangeRate`, `exchangeRateDate`, `customerNumber`, `customerName`, `itemNumber`, `DeferAmount`, `RecognizeAmount`, `referenceDocoumentId`, `isCancelled`, `BundleParentId`, `BundleConfigurationId` FROM `revenueContractBillingDetails`";
    public Optional<RevenueContractBillingDetailsRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractBillingDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractBillingDetailsRecord.TABLE_NAME, String.valueOf(id), RevenueContractBillingDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractBillingDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractBillingDetailsRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
