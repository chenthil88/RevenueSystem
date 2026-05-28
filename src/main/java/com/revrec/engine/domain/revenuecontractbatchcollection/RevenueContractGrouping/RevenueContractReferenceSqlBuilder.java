package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds SQL queries for revenue contract reference lookups
 * Separates SQL construction logic from service orchestration
 */
public class RevenueContractReferenceSqlBuilder {

    private static final String REFERENCE_TABLE = "revenueContractReferenceDetails";
    private static final String KEY_COL = "key";
    private static final String VALUE_COL = "RevenueContractId";

    /**
     * Build batch lookup query using UNION for multiple ID fields
     */
    public BatchLookupQuery buildBatchLookupQuery(
            List<String> salesOrderIds,
            List<String> invoiceIds,
            List<String> originalInvoiceIds,
            List<String> originalSalesOrderIds) {

        if (allEmpty(salesOrderIds, invoiceIds, originalInvoiceIds, originalSalesOrderIds)) {
            return new BatchLookupQuery("", new HashMap<>());
        }

        StringBuilder sql = new StringBuilder();

        if (!salesOrderIds.isEmpty()) {
            sql.append(String.format(
                    "SELECT \"salesOrderId\" as %s, \"%s\" as %s FROM \"%s\" WHERE \"salesOrderId\" IN (:salesOrderIds) ",
                    KEY_COL, VALUE_COL, VALUE_COL, REFERENCE_TABLE));
        }

        if (!invoiceIds.isEmpty()) {
            if (sql.length() > 0) sql.append("UNION ALL ");
            sql.append(String.format(
                    "SELECT \"invoiceId\", \"%s\" FROM \"%s\" WHERE \"invoiceId\" IN (:invoiceIds) ",
                    VALUE_COL, REFERENCE_TABLE));
        }

        if (!originalInvoiceIds.isEmpty()) {
            if (sql.length() > 0) sql.append("UNION ALL ");
            sql.append(String.format(
                    "SELECT \"originalInvoiceId\", \"%s\" FROM \"%s\" WHERE \"originalInvoiceId\" IN (:originalInvoiceIds) ",
                    VALUE_COL, REFERENCE_TABLE));
        }

        if (!originalSalesOrderIds.isEmpty()) {
            if (sql.length() > 0) sql.append("UNION ALL ");
            sql.append(String.format(
                    "SELECT \"originalSalesOrderId\", \"%s\" FROM \"%s\" WHERE \"originalSalesOrderId\" IN (:originalSalesOrderIds) ",
                    VALUE_COL, REFERENCE_TABLE));
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

    /**
     * Build single-record lookup query
     */
    public String buildSingleLookupQuery() {
        return String.format(
                "SELECT \"%s\" FROM \"%s\" WHERE " +
                "(\"salesOrderId\" = :salesOrderId OR " +
                "\"invoiceId\" = :invoiceId OR " +
                "\"invoiceId\" = :originalInvoiceId OR " +
                "\"salesOrderId\" = :originalSalesOrderId) " +
                "LIMIT 1",
                VALUE_COL, REFERENCE_TABLE);
    }

    /**
     * Build mark-as-processed query
     */
    public String buildMarkProcessedQuery() {
        return "UPDATE \"RevRecStage\" SET \"processsedFlag\" = :flag WHERE \"id\" IN (:ids)";
    }

    /**
     * Build mark-with-error query
     */
    public String buildMarkErrorQuery() {
        return "UPDATE \"RevRecStage\" SET \"processsedFlag\" = 'E', \"errorMessage\" = :errorMessage WHERE \"id\" IN (:ids)";
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
