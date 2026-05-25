package com.revrec.engine.common.service.RevenueContractReferenceDetails;

import com.revrec.engine.domain.service.RevenueContractReferenceDetails.RevenueContractReferenceDetailsRecord;
import com.revrec.engine.domain.service.RevRecStage.RevRecStageRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class RevenueContractReferenceDetailsService {

    private static final String NEXT_ID = "SELECT NEXTVAL(revenue_contract_reference_details_id_seq)";

    private static final String INSERT =
            """
            INSERT INTO `revenueContractReferenceDetails`
                (`Id`, `RevenueContractId`, `salesOrderId`, `invoiceId`, `createdPeriodId`, `IsActive`, `CreatedAt`, `UpdatedAt`)
            VALUES
                (:id, :revenueContractId, :salesOrderId, :invoiceId, :createdPeriodId, :isActive, :createdAt, :updatedAt)
            """;

    private final NamedParameterJdbcTemplate jdbc;

    public RevenueContractReferenceDetailsService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public long nextId() {
        Long id = jdbc.queryForObject(NEXT_ID, Map.of(), Long.class);
        if (id == null) {
            throw new IllegalStateException("NEXTVAL(revenue_contract_reference_details_id_seq) returned null");
        }
        return id;
    }

    public void insertAll(List<RevenueContractReferenceDetailsRecord> records) {
        if (records.isEmpty()) {
            return;
        }
        SqlParameterSource[] batch = records.stream()
                .map(record -> new MapSqlParameterSource()
                        .addValue("id", record.id())
                        .addValue("revenueContractId", record.revenueContractId())
                        .addValue("salesOrderId", record.salesOrderId())
                        .addValue("invoiceId", record.invoiceId())
                        .addValue("createdPeriodId", record.createdPeriodId())
                        .addValue("isActive", record.isActive())
                        .addValue("createdAt", record.createdAt())
                        .addValue("updatedAt", record.updatedAt()))
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(INSERT, batch);
    }

    /**
     * Adds a reference row for the stage line when sales order and/or invoice is present and not yet captured
     * for this contract in the current batch pass.
     */
    public void captureReferenceDetail(
            RevRecStageRecord stage,
            long revenueContractId,
            LocalDateTime timestamp,
            Set<String> referenceKeys,
            List<RevenueContractReferenceDetailsRecord> target) {
        String salesOrderId = blankToNull(stage.salesOrderId());
        String invoiceId = blankToNull(stage.invoiceId());
        if (salesOrderId == null && invoiceId == null) {
            return;
        }
        String referenceKey = revenueContractId + "|" + salesOrderId + "|" + invoiceId;
        if (!referenceKeys.add(referenceKey)) {
            return;
        }
        target.add(new RevenueContractReferenceDetailsRecord(
                nextId(),
                revenueContractId,
                salesOrderId,
                invoiceId,
                stage.createdPeriodId(),
                true,
                timestamp,
                timestamp));
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
