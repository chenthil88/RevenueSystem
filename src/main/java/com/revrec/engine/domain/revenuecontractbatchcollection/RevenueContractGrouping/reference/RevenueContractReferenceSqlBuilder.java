package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RevenueContractReferenceSqlBuilder {

    public BatchLookupQuery buildBatchLookupQuery(
            List<String> salesOrderIds,
            List<String> invoiceIds,
            List<String> originalInvoiceIds,
            List<String> originalSalesOrderIds) {

        if (allEmpty(salesOrderIds, invoiceIds, originalInvoiceIds, originalSalesOrderIds)) {
            return new BatchLookupQuery("", new HashMap<>());
        }

        String table = RevenueContractGroupingConstants.REVENUE_CONTRACT_REFERENCE_TABLE;
        String keyCol = RevenueContractGroupingConstants.REFERENCE_LOOKUP_KEY_COLUMN;
        String valueCol = RevenueContractGroupingConstants.REFERENCE_LOOKUP_VALUE_COLUMN;

        StringBuilder sql = new StringBuilder();
        if (!salesOrderIds.isEmpty()) {
            sql.append(String.format(
                    "SELECT \"salesOrderId\" as %s, \"%s\" as %s FROM \"%s\" WHERE \"salesOrderId\" IN (:salesOrderIds) ",
                    keyCol, valueCol, valueCol, table));
        }
        if (!invoiceIds.isEmpty()) {
            if (!sql.isEmpty()) {
                sql.append("UNION ALL ");
            }
            sql.append(String.format(
                    "SELECT \"invoiceId\", \"%s\" FROM \"%s\" WHERE \"invoiceId\" IN (:invoiceIds) ",
                    valueCol, table));
        }
        if (!originalInvoiceIds.isEmpty()) {
            if (!sql.isEmpty()) {
                sql.append("UNION ALL ");
            }
            sql.append(String.format(
                    "SELECT \"originalInvoiceId\", \"%s\" FROM \"%s\" WHERE \"originalInvoiceId\" IN (:originalInvoiceIds) ",
                    valueCol, table));
        }
        if (!originalSalesOrderIds.isEmpty()) {
            if (!sql.isEmpty()) {
                sql.append("UNION ALL ");
            }
            sql.append(String.format(
                    "SELECT \"originalSalesOrderId\", \"%s\" FROM \"%s\" WHERE \"originalSalesOrderId\" IN (:originalSalesOrderIds) ",
                    valueCol, table));
        }

        Map<String, Object> params = new HashMap<>();
        if (!salesOrderIds.isEmpty()) {
            params.put("salesOrderIds", salesOrderIds);
        }
        if (!invoiceIds.isEmpty()) {
            params.put("invoiceIds", invoiceIds);
        }
        if (!originalInvoiceIds.isEmpty()) {
            params.put("originalInvoiceIds", originalInvoiceIds);
        }
        if (!originalSalesOrderIds.isEmpty()) {
            params.put("originalSalesOrderIds", originalSalesOrderIds);
        }

        return new BatchLookupQuery(sql.toString(), params);
    }

    public String buildSingleLookupQuery() {
        String table = RevenueContractGroupingConstants.REVENUE_CONTRACT_REFERENCE_TABLE;
        String valueCol = RevenueContractGroupingConstants.REFERENCE_LOOKUP_VALUE_COLUMN;
        return String.format(
                "SELECT \"%s\" FROM \"%s\" WHERE "
                        + "(\"salesOrderId\" = :salesOrderId OR "
                        + "\"invoiceId\" = :invoiceId OR "
                        + "\"invoiceId\" = :originalInvoiceId OR "
                        + "\"salesOrderId\" = :originalSalesOrderId) "
                        + "LIMIT 1",
                valueCol, table);
    }

    public String buildMarkProcessedQuery() {
        return "UPDATE \"" + RevenueContractGroupingConstants.REV_REC_STAGE_TABLE
                + "\" SET \"processsedFlag\" = :flag WHERE \"id\" IN (:ids)";
    }

    public String buildMarkErrorQuery() {
        return "UPDATE \"" + RevenueContractGroupingConstants.REV_REC_STAGE_TABLE + "\" SET \"processsedFlag\" = '"
                + RevenueContractGroupingConstants.PROCESSED_FLAG_ERROR
                + "', \"errorMessage\" = :errorMessage WHERE \"id\" IN (:ids)";
    }

    private boolean allEmpty(List<?>... lists) {
        for (List<?> list : lists) {
            if (list != null && !list.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static class BatchLookupQuery {
        public final String sql;
        public final Map<String, Object> params;

        public BatchLookupQuery(String sql, Map<String, Object> params) {
            this.sql = sql;
            this.params = params;
        }

        public boolean isEmpty() {
            return sql == null || sql.isBlank();
        }
    }
}
