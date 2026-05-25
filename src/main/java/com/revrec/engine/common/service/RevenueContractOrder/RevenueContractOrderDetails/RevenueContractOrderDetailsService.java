package com.revrec.engine.common.service.RevenueContractOrder.RevenueContractOrderDetails;

import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails.RevenueContractOrderDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails.RevenueContractOrderDetailsRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractOrderDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractOrderDetailsRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractOrderDetailsService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractOrderDetailsRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `transactionType`, `revenueContractId`, `salesOrderId`, `salesOrderNumber`, `salesOrderDate`, `SellPrice`, `ListPrice`, `UnitSellPrice`, `UnitListPrice`, `transactionCurrency`, `FunctionalCurrency`, `exchangeRate`, `globalexchangeRate`, `exchangeRateDate`, `customerNumber`, `customerName`, `itemNumber`, `DeferAmount`, `RecognizeAmount`, `BilledDeferAmount`, `BilledRecognizeAmount`, `BundleConfigurationId`, `OrderQuantity`, `InvoiceQuantity`, `ReturnQuantity`, `RevenueStartDate`, `RevenueEndDate`, `Duration`, `Term`, `BookId`, `OrganizationId`, `DiscountAmount`, `DiscountPercentage`, `bundleParentId`, `companyCode`, `IsCancelOrder`, `IsReturnOrder`, `IsEligibleForAllocation`, `createdPeriodId`, `CumulativeCarveAmount`, `CumulativeAllocatedPrice`, `Comments`, `CreatedBy`, `CreatedAt`, `UpdatedBy`, `UpdatedAt`, `ProductCategory`, `ProductClass`, `ProductFamily`, `ProductLine`, `BusinessUnit`, `ContractModificationDate`, `SubscriptionId`, `SubscriptionName`, `SubscriptionVersion`, `SubscriptionStartDate`, `SubscriptionEndDate` FROM `revenueContractOrderDetails`";
    public Optional<RevenueContractOrderDetailsRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractOrderDetailsRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractOrderDetailsRecord.TABLE_NAME, String.valueOf(id), RevenueContractOrderDetailsRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractOrderDetailsRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractOrderDetailsRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
