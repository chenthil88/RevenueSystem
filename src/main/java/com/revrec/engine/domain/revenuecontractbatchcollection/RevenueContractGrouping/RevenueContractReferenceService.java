package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

/**
 * Service for resolving revenue contract IDs from reference details
 */
@Service
public class RevenueContractReferenceService {

    private final NamedParameterJdbcTemplate jdbc;

    public RevenueContractReferenceService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Resolve revenue contract ID from reference details using multiple lookup strategies
     */
    public Long resolveRevenueContractId(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId) {

        String sql = "SELECT \"RevenueContractId\" FROM \"revenueContractReferenceDetails\" " +
                "WHERE (\"salesOrderId\" = :salesOrderId OR " +
                "       \"invoiceId\" = :invoiceId OR " +
                "       \"invoiceId\" = :originalInvoiceId OR " +
                "       \"salesOrderId\" = :originalSalesOrderId) " +
                "LIMIT 1";

        try {
            return jdbc.query(
                    sql,
                    Map.of(
                            "salesOrderId", salesOrderId,
                            "invoiceId", invoiceId,
                            "originalInvoiceId", originalInvoiceId,
                            "originalSalesOrderId", originalSalesOrderId
                    ),
                    (rs, rowNum) -> rs.getLong("RevenueContractId"))
                    .stream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if reference exists
     */
    public boolean referenceExists(String salesOrderId, String invoiceId) {
        String sql = "SELECT COUNT(*) FROM \"revenueContractReferenceDetails\" " +
                "WHERE \"salesOrderId\" = :salesOrderId OR \"invoiceId\" = :invoiceId";

        Integer count = jdbc.queryForObject(
                sql,
                Map.of("salesOrderId", salesOrderId, "invoiceId", invoiceId),
                Integer.class);

        return count != null && count > 0;
    }
}
