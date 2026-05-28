package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps ResultSet rows to reference lookup results
 * Separates data mapping logic from service orchestration
 */
public class RevenueContractReferenceMapper {

    private static final String KEY_COLUMN = "key";
    private static final String VALUE_COLUMN = "RevenueContractId";

    /**
     * Extract reference mappings from ResultSet
     * Returns a map of [lookup-key -> revenueContractId]
     */
    public Map<String, Long> mapResultSetToReferences(ResultSet rs) throws SQLException {
        Map<String, Long> result = new HashMap<>();

        while (rs.next()) {
            String key = rs.getString(KEY_COLUMN);
            Long contractId = rs.getLong(VALUE_COLUMN);

            if (key != null && !key.isBlank()) {
                result.put(key, contractId);
            }
        }

        return result;
    }

    /**
     * Apply resolved contract IDs to records
     * Tries lookup keys in priority order: salesOrderId, invoiceId, originalInvoiceId, originalSalesOrderId
     */
    public void applyResolutionsToRecords(
            Iterable<RevRecStageGroupingRecord> records,
            Map<String, Long> referenceMap) {

        for (RevRecStageGroupingRecord record : records) {
            if (record.getRevenueContractId() != null) {
                continue; // Already has a value
            }

            Long resolved = tryResolveFromMap(record, referenceMap);
            if (resolved != null) {
                record.setRevenueContractId(resolved);
            }
        }
    }

    private Long tryResolveFromMap(RevRecStageGroupingRecord record, Map<String, Long> referenceMap) {
        Long resolved = referenceMap.get(record.getSalesOrderId());
        if (resolved != null) return resolved;

        resolved = referenceMap.get(record.getInvoiceId());
        if (resolved != null) return resolved;

        resolved = referenceMap.get(record.getOriginalInvoiceId());
        if (resolved != null) return resolved;

        return referenceMap.get(record.getOriginalSalesOrderId());
    }
}
