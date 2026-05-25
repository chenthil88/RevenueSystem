package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RevenueContractReferenceMapper {

    public Map<String, Long> mapResultSetToReferences(ResultSet rs) throws SQLException {
        Map<String, Long> result = new HashMap<>();
        while (rs.next()) {
            String key = rs.getString(RevenueContractGroupingConstants.REFERENCE_LOOKUP_KEY_COLUMN);
            Long contractId = rs.getLong(RevenueContractGroupingConstants.REFERENCE_LOOKUP_VALUE_COLUMN);
            if (key != null && !key.isBlank()) {
                result.put(key, contractId);
            }
        }
        return result;
    }

    public void applyResolutionsToRecords(
            Iterable<RevRecStageGroupingRecord> records, Map<String, Long> referenceMap) {
        for (RevRecStageGroupingRecord record : records) {
            if (record.getRevenueContractId() != null) {
                continue;
            }
            Long resolved = tryResolveFromMap(record, referenceMap);
            if (resolved != null) {
                record.setRevenueContractId(resolved);
            }
        }
    }

    private Long tryResolveFromMap(RevRecStageGroupingRecord record, Map<String, Long> referenceMap) {
        Long resolved = referenceMap.get(record.getSalesOrderId());
        if (resolved != null) {
            return resolved;
        }
        resolved = referenceMap.get(record.getInvoiceId());
        if (resolved != null) {
            return resolved;
        }
        resolved = referenceMap.get(record.getOriginalInvoiceId());
        if (resolved != null) {
            return resolved;
        }
        return referenceMap.get(record.getOriginalSalesOrderId());
    }
}
