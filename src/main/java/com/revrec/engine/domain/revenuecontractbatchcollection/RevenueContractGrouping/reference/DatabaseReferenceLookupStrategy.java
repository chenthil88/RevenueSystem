package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Slf4j
public class DatabaseReferenceLookupStrategy implements RevenueContractReferenceLookupStrategy {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractReferenceSqlBuilder sqlBuilder;
    private final RevenueContractReferenceMapper mapper;

    public DatabaseReferenceLookupStrategy(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractReferenceSqlBuilder sqlBuilder,
            RevenueContractReferenceMapper mapper) {
        this.jdbc = jdbc;
        this.sqlBuilder = sqlBuilder;
        this.mapper = mapper;
    }

    @Override
    public Optional<Long> resolve(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId) {
        String sql = sqlBuilder.buildSingleLookupQuery();
        Map<String, Object> params = Map.of(
                "salesOrderId", salesOrderId,
                "invoiceId", invoiceId,
                "originalInvoiceId", originalInvoiceId,
                "originalSalesOrderId", originalSalesOrderId);
        try {
            List<Long> results = jdbc.query(
                    sql, params, (rs, rowNum) -> rs.getLong(RevenueContractGroupingConstants.REFERENCE_LOOKUP_VALUE_COLUMN));
            return results.stream().findFirst();
        } catch (DataAccessException e) {
            log.warn("Failed to resolve reference for salesOrderId={}, invoiceId={}", salesOrderId, invoiceId, e);
            return Optional.empty();
        }
    }

    @Override
    public void resolveBatch(List<RevRecStageGroupingRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }

        Set<String> salesOrderIds = new HashSet<>();
        Set<String> invoiceIds = new HashSet<>();
        Set<String> originalInvoiceIds = new HashSet<>();
        Set<String> originalSalesOrderIds = new HashSet<>();

        records.stream()
                .filter(r -> r.getRevenueContractId() == null)
                .forEach(r -> {
                    addNonNull(salesOrderIds, r.getSalesOrderId());
                    addNonNull(invoiceIds, r.getInvoiceId());
                    addNonNull(originalInvoiceIds, r.getOriginalInvoiceId());
                    addNonNull(originalSalesOrderIds, r.getOriginalSalesOrderId());
                });

        if (nothingToResolve(salesOrderIds, invoiceIds, originalInvoiceIds, originalSalesOrderIds)) {
            return;
        }

        Map<String, Long> referenceMap = loadReferenceMap(
                new ArrayList<>(salesOrderIds),
                new ArrayList<>(invoiceIds),
                new ArrayList<>(originalInvoiceIds),
                new ArrayList<>(originalSalesOrderIds));
        mapper.applyResolutionsToRecords(records, referenceMap);
    }

    @Override
    public String getStrategyName() {
        return RevenueContractGroupingConstants.DATABASE_REFERENCE_LOOKUP_STRATEGY;
    }

    @Override
    public boolean canHandle(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId) {
        return true;
    }

    private Map<String, Long> loadReferenceMap(
            List<String> salesOrderIds,
            List<String> invoiceIds,
            List<String> originalInvoiceIds,
            List<String> originalSalesOrderIds) {

        RevenueContractReferenceSqlBuilder.BatchLookupQuery query = sqlBuilder.buildBatchLookupQuery(
                salesOrderIds, invoiceIds, originalInvoiceIds, originalSalesOrderIds);
        if (query.isEmpty()) {
            return new HashMap<>();
        }

        try {
            Map<String, Long> result = new HashMap<>();
            jdbc.query(query.sql, query.params, (rs, rowNum) -> {
                try {
                    mapper.mapResultSetToReferences(rs).forEach(result::putIfAbsent);
                } catch (Exception e) {
                    log.warn("Error mapping reference result row", e);
                }
                return null;
            });
            return result;
        } catch (DataAccessException e) {
            log.error("Failed to load reference map from database", e);
            throw new ReferenceResolutionException("Failed to load revenue contract references", e);
        }
    }

    private void addNonNull(Set<String> set, String value) {
        if (value != null && !value.isBlank()) {
            set.add(value);
        }
    }

    private boolean nothingToResolve(Set<?>... sets) {
        return Arrays.stream(sets).allMatch(Set::isEmpty);
    }
}
