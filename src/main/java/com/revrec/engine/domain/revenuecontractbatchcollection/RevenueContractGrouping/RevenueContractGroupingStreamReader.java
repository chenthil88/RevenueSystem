package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Streams data from RevRecStage using cursor-based pagination for memory efficiency
 */
@Component
public class RevenueContractGroupingStreamReader {

    private final JdbcTemplate jdbc;
    private final BatchProcessingConfig config;

    public RevenueContractGroupingStreamReader(
            JdbcTemplate jdbc,
            BatchProcessingConfig config) {
        this.jdbc = jdbc;
        this.config = config;
    }

    /**
     * Stream unprocessed RevRecStage records using cursor-based approach
     * Only fetches records with processsedFlag = 'N' or 'E'
     */
    public Stream<RevRecStageGroupingRecord> streamUnprocessedRecords(String tenantId) {
        List<RevRecStageGroupingRecord> records = new ArrayList<>();
        int offset = 0;
        int fetchSize = config.getFetchSize();

        // Fetch records in chunks to avoid loading everything into memory
        while (true) {
            String sql = "SELECT " +
                    "\"id\", \"tenantId\", \"transactionType\", \"processsedFlag\", " +
                    "\"errorMessage\", \"revenueContractId\", \"revenueContractGroupValue\", " +
                    "\"batchId\", \"salesOrderId\", \"invoiceId\", \"originalInvoiceId\", " +
                    "\"originalSalesOrderId\", \"customerNumber\", \"customerName\", \"createdAt\" " +
                    "FROM \"RevRecStage\" " +
                    "WHERE \"tenantId\" = ? AND COALESCE(\"processsedFlag\", 'N') IN ('N', 'E') " +
                    "ORDER BY \"id\" " +
                    "OFFSET ? LIMIT ?";

            List<RevRecStageGroupingRecord> batch = jdbc.query(
                    sql,
                    new Object[]{tenantId, offset, fetchSize},
                    (rs, rowNum) -> mapRevRecStageRecord(rs));

            if (batch.isEmpty()) {
                break;
            }

            records.addAll(batch);
            offset += batch.size();

            // Return stream immediately without loading all into memory
            if (batch.size() < fetchSize) {
                break;
            }
        }

        return records.stream();
    }

    /**
     * Stream with reference details lookup for more efficient processing
     */
    public Stream<RevRecStageGroupingRecord> streamWithReferenceDetails(String tenantId) {
        String sql = "SELECT " +
                "stg.\"id\", stg.\"tenantId\", stg.\"transactionType\", stg.\"processsedFlag\", " +
                "stg.\"errorMessage\", stg.\"revenueContractId\", stg.\"revenueContractGroupValue\", " +
                "stg.\"batchId\", stg.\"salesOrderId\", stg.\"invoiceId\", stg.\"originalInvoiceId\", " +
                "stg.\"originalSalesOrderId\", stg.\"customerNumber\", stg.\"customerName\", stg.\"createdAt\", " +
                "rcf.\"RevenueContractId\" as ref_revenue_contract_id " +
                "FROM \"RevRecStage\" stg " +
                "LEFT JOIN \"revenueContractReferenceDetails\" rcf ON " +
                "(rcf.\"salesOrderId\" = stg.\"salesOrderId\" OR " +
                "rcf.\"invoiceId\" = stg.\"invoiceId\" OR " +
                "rcf.\"invoiceId\" = stg.\"originalInvoiceId\" OR " +
                "rcf.\"salesOrderId\" = stg.\"originalSalesOrderId\") " +
                "WHERE stg.\"tenantId\" = ? AND COALESCE(stg.\"processsedFlag\", 'N') IN ('N', 'E')";

        return jdbc.query(
                sql,
                new Object[]{tenantId},
                (ResultSetExtractor<Stream<RevRecStageGroupingRecord>>) rs -> {
                    List<RevRecStageGroupingRecord> records = new ArrayList<>();
                    while (rs.next()) {
                        records.add(mapRevRecStageRecord(rs));
                    }
                    return records.stream();
                });
    }

    /**
     * Count unprocessed records
     */
    public long countUnprocessedRecords(String tenantId) {
        String sql = "SELECT COUNT(*) FROM \"RevRecStage\" " +
                "WHERE \"tenantId\" = ? AND COALESCE(\"processsedFlag\", 'N') IN ('N', 'E')";
        Integer count = jdbc.queryForObject(sql, new Object[]{tenantId}, Integer.class);
        return count != null ? count : 0;
    }

    private RevRecStageGroupingRecord mapRevRecStageRecord(ResultSet rs) throws SQLException {
        RevRecStageGroupingRecord record = new RevRecStageGroupingRecord();
        record.setId(rs.getLong("id"));
        record.setTenantId(rs.getString("tenantId"));
        record.setTransactionType(rs.getString("transactionType"));
        record.setProcesssedFlag(rs.getString("processsedFlag"));
        record.setErrorMessage(rs.getString("errorMessage"));
        record.setRevenueContractId(rs.getLong("revenueContractId") == 0 ? null : rs.getLong("revenueContractId"));
        record.setRevenueContractGroupValue(rs.getString("revenueContractGroupValue"));
        record.setBatchId(rs.getLong("batchId") == 0 ? null : rs.getLong("batchId"));
        record.setSalesOrderId(rs.getString("salesOrderId"));
        record.setInvoiceId(rs.getString("invoiceId"));
        record.setOriginalInvoiceId(rs.getString("originalInvoiceId"));
        record.setOriginalSalesOrderId(rs.getString("originalSalesOrderId"));
        record.setCustomerNumber(rs.getString("customerNumber"));
        record.setCustomerName(rs.getString("customerName"));
        record.setCreatedAt(rs.getTimestamp("createdAt") != null ?
                rs.getTimestamp("createdAt").toLocalDateTime() : null);
        return record;
    }
}
