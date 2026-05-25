package com.revrec.engine.common.metadataservice.RevenueContractGroupingHierarchy;

import com.revrec.engine.domain.metadataservice.RevenueContractGroupingHierarchy.RevenueContractGroupingHierarchyRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.RevenueContractGroupingHierarchy.RevenueContractGroupingHierarchyRecordMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractGroupingHierarchyService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractGroupingHierarchyRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractGroupingHierarchyService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractGroupingHierarchyRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `Id`, `sequence`, `RevenueContractGroupingTemplateId`, `GroupingFields`, `createdPeriodId`, `IsActive`, `CreatedAt`, `UpdatedAt` FROM `RevenueContractGroupingHierarchy`";
    
    private List<RevenueContractGroupingHierarchyRecord> findByRevenueContractGroupingTemplateId(Long revenueContractGroupingTemplateId) {
        return jdbc.query(
                SELECT + " WHERE `RevenueContractGroupingTemplateId` = :templateId AND `IsActive` = TRUE",
                Map.of("templateId", revenueContractGroupingTemplateId),
                rowMapper);
    }

    /**
     * Returns {@code GroupingFields} from the lowest {@code sequence} active hierarchy row.
     */
    public String resolveGroupingFields(Long revenueContractGroupingTemplateId) {
        return findByRevenueContractGroupingTemplateId(revenueContractGroupingTemplateId).stream()
                .filter(row -> row.groupingFields() != null && !row.groupingFields().isBlank())
                .min(Comparator.comparing(
                        RevenueContractGroupingHierarchyRecord::sequence,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(RevenueContractGroupingHierarchyRecord::groupingFields)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No active GroupingFields for template id: " + revenueContractGroupingTemplateId));
    }
}
