package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.stream;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.config.BatchProcessingConfig;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class RevenueContractGroupingStreamReader {

    private final JdbcTemplate jdbc;
    private final BatchProcessingConfig config;

    public RevenueContractGroupingStreamReader(JdbcTemplate jdbc, BatchProcessingConfig config) {
        this.jdbc = jdbc;
        this.config = config;
    }

    public Stream<RevRecStageGroupingRecord> streamUnprocessedRecords(String tenantId) {
        List<RevRecStageGroupingRecord> records = new ArrayList<>();
        int offset = 0;
        int fetchSize = config.getFetchSize();
        String table = RevenueContractGroupingConstants.REV_REC_STAGE_TABLE;
        String newFlag = RevenueContractGroupingConstants.PROCESSED_FLAG_NEW;
        String errorFlag = RevenueContractGroupingConstants.PROCESSED_FLAG_ERROR;

        while (true) {
            String sql = "SELECT "
                    + "\"id\", \"tenantId\", \"transactionType\", \"processsedFlag\", "
                    + "\"errorMessage\", \"revenueContractId\", \"revenueContractGroupValue\", "
                    + "\"batchId\", \"salesOrderId\", \"invoiceId\", \"originalInvoiceId\", "
                    + "\"originalSalesOrderId\", \"customerNumber\", \"customerName\", \"createdAt\" "
                    + "FROM \"" + table + "\" "
                    + "WHERE \"tenantId\" = ? AND COALESCE(\"processsedFlag\", '" + newFlag + "') IN ('"
                    + newFlag + "', '" + errorFlag + "') "
                    + "ORDER BY \"id\" "
                    + "OFFSET ? LIMIT ?";

            List<RevRecStageGroupingRecord> batch =
                    jdbc.query(sql, new Object[] {tenantId, offset, fetchSize}, (rs, rowNum) -> mapRevRecStageRecord(rs));

            if (batch.isEmpty()) {
                break;
            }
            records.addAll(batch);
            offset += batch.size();
            if (batch.size() < fetchSize) {
                break;
            }
        }
        return records.stream();
    }

    public Stream<RevRecStageGroupingRecord> streamWithReferenceDetails(String tenantId) {
        String stageTable = RevenueContractGroupingConstants.REV_REC_STAGE_TABLE;
        String refTable = RevenueContractGroupingConstants.REVENUE_CONTRACT_REFERENCE_TABLE;
        String newFlag = RevenueContractGroupingConstants.PROCESSED_FLAG_NEW;
        String errorFlag = RevenueContractGroupingConstants.PROCESSED_FLAG_ERROR;

        String sql = "SELECT "
                + "stg.\"id\", stg.\"tenantId\", stg.\"transactionType\", stg.\"processsedFlag\", "
                + "stg.\"errorMessage\", stg.\"revenueContractId\", stg.\"revenueContractGroupValue\", "
                + "stg.\"batchId\", stg.\"salesOrderId\", stg.\"invoiceId\", stg.\"originalInvoiceId\", "
                + "stg.\"originalSalesOrderId\", stg.\"customerNumber\", stg.\"customerName\", stg.\"createdAt\", "
                + "rcf.\"" + RevenueContractGroupingConstants.REFERENCE_LOOKUP_VALUE_COLUMN + "\" as ref_revenue_contract_id "
                + "FROM \"" + stageTable + "\" stg "
                + "LEFT JOIN \"" + refTable + "\" rcf ON "
                + "(rcf.\"salesOrderId\" = stg.\"salesOrderId\" OR "
                + "rcf.\"invoiceId\" = stg.\"invoiceId\" OR "
                + "rcf.\"invoiceId\" = stg.\"originalInvoiceId\" OR "
                + "rcf.\"salesOrderId\" = stg.\"originalSalesOrderId\") "
                + "WHERE stg.\"tenantId\" = ? AND COALESCE(stg.\"processsedFlag\", '" + newFlag + "') IN ('"
                + newFlag + "', '" + errorFlag + "')";

        return jdbc.query(
                sql,
                new Object[] {tenantId},
                (ResultSetExtractor<Stream<RevRecStageGroupingRecord>>) rs -> {
                    List<RevRecStageGroupingRecord> records = new ArrayList<>();
                    while (rs.next()) {
                        records.add(mapRevRecStageRecord(rs));
                    }
                    return records.stream();
                });
    }

    public long countUnprocessedRecords(String tenantId) {
        String table = RevenueContractGroupingConstants.REV_REC_STAGE_TABLE;
        String newFlag = RevenueContractGroupingConstants.PROCESSED_FLAG_NEW;
        String errorFlag = RevenueContractGroupingConstants.PROCESSED_FLAG_ERROR;
        String sql = "SELECT COUNT(*) FROM \"" + table + "\" "
                + "WHERE \"tenantId\" = ? AND COALESCE(\"processsedFlag\", '" + newFlag + "') IN ('"
                + newFlag + "', '" + errorFlag + "')";
        Integer count = jdbc.queryForObject(sql, new Object[] {tenantId}, Integer.class);
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
        record.setCreatedAt(
                rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null);
        return record;
    }
}
