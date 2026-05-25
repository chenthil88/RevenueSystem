package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import com.revrec.engine.common.metadataservice.RevenueContractGroupingHierarchy.RevenueContractGroupingHierarchyService;
import com.revrec.engine.common.metadataservice.RevenueContractGroupingTemplate.RevenueContractGroupingTemplateService;
import com.revrec.engine.common.service.RevenueContractGroupDetails.RevenueContractGroupDetailsService;
import com.revrec.engine.common.service.RevenueContractHeader.RevenueContractHeaderService;
import com.revrec.engine.common.service.RevenueContractReferenceDetails.RevenueContractReferenceDetailsService;
import com.revrec.engine.common.service.RevRecStage.RevRecStageService;
import com.revrec.engine.common.service.RevRecStage.RevRecStageService.StageRevenueContractAssignment;
import com.revrec.engine.domain.metadataservice.RevenueContractGroupingTemplate.RevenueContractGroupingTemplateRecord;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevenueContractGroupingResult;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.rule.GroupingRuleSqlBuilder;
import com.revrec.engine.domain.service.RevenueContractGroupDetails.RevenueContractGroupDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractReferenceDetails.RevenueContractReferenceDetailsRecord;
import com.revrec.engine.domain.service.RevRecStage.RevRecStageRecord;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Batch orchestration: resolves grouping template/hierarchy, stamps stage group values, assigns new
 * revenue contracts per group, and persists group/reference details.
 */
@Service
public class RevenueContractGroupingService {

    private final RevRecStageService revRecStageService;
    private final RevenueContractHeaderService revenueContractHeaderService;
    private final RevenueContractGroupDetailsService revenueContractGroupDetailsService;
    private final RevenueContractReferenceDetailsService revenueContractReferenceDetailsService;
    private final RevenueContractGroupingTemplateService groupingTemplateService;
    private final RevenueContractGroupingHierarchyService groupingHierarchyService;
    private final GroupingRuleSqlBuilder groupingRuleSqlBuilder;

    public RevenueContractGroupingService(
            RevRecStageService revRecStageService,
            RevenueContractHeaderService revenueContractHeaderService,
            RevenueContractGroupDetailsService revenueContractGroupDetailsService,
            RevenueContractReferenceDetailsService revenueContractReferenceDetailsService,
            RevenueContractGroupingTemplateService groupingTemplateService,
            RevenueContractGroupingHierarchyService groupingHierarchyService,
            GroupingRuleSqlBuilder groupingRuleSqlBuilder) {
        this.revRecStageService = revRecStageService;
        this.revenueContractHeaderService = revenueContractHeaderService;
        this.revenueContractGroupDetailsService = revenueContractGroupDetailsService;
        this.revenueContractReferenceDetailsService = revenueContractReferenceDetailsService;
        this.groupingTemplateService = groupingTemplateService;
        this.groupingHierarchyService = groupingHierarchyService;
        this.groupingRuleSqlBuilder = groupingRuleSqlBuilder;
    }

    @Transactional
    public RevenueContractGroupingResult applyGrouping(long batchId, String templateName) {
        RevenueContractGroupingTemplateRecord template = groupingTemplateService
                .findByName(templateName)
                .filter(t -> Boolean.TRUE.equals(t.isActive()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Active RevenueContractGroupingTemplate not found for name: " + templateName));

        String groupingFields = groupingHierarchyService.resolveGroupingFields(template.id());
        String groupingRuleSql = groupingRuleSqlBuilder.build(groupingFields);
        int groupValuesStamped = revRecStageService.applyContractGrouping(batchId, groupingRuleSql);
        return assignRevenueContractsForBatch(batchId, groupValuesStamped);
    }

    private RevenueContractGroupingResult assignRevenueContractsForBatch(long batchId, int groupValuesStamped) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Long> groupValueToContractId = new LinkedHashMap<>();
        Set<String> registeredGroupValues = new HashSet<>();
        Set<String> referenceKeys = new HashSet<>();

        List<RevenueContractGroupDetailsRecord> groupDetailsRecords = new ArrayList<>();
        List<RevenueContractReferenceDetailsRecord> referenceDetailsRecords = new ArrayList<>();
        List<StageRevenueContractAssignment> stageAssignments = new ArrayList<>();

        try (Stream<RevRecStageRecord> stageLines = revRecStageService.streamUnassignedByGroupValue(batchId)) {
            stageLines.forEach(stage -> collectAssignments(
                    stage,
                    now,
                    groupValueToContractId,
                    registeredGroupValues,
                    referenceKeys,
                    groupDetailsRecords,
                    referenceDetailsRecords,
                    stageAssignments));
        }

        revenueContractGroupDetailsService.insertAll(groupDetailsRecords);
        revenueContractReferenceDetailsService.insertAll(referenceDetailsRecords);
        int stagesUpdated = revRecStageService.bulkUpdateRevenueContractIds(stageAssignments);

        return new RevenueContractGroupingResult(
                groupValuesStamped, stagesUpdated, List.copyOf(groupDetailsRecords), List.copyOf(referenceDetailsRecords));
    }

    private void collectAssignments(
            RevRecStageRecord stage,
            LocalDateTime now,
            Map<String, Long> groupValueToContractId,
            Set<String> registeredGroupValues,
            Set<String> referenceKeys,
            List<RevenueContractGroupDetailsRecord> groupDetailsRecords,
            List<RevenueContractReferenceDetailsRecord> referenceDetailsRecords,
            List<StageRevenueContractAssignment> stageAssignments) {
        String groupingValue = normalizeGroupingValue(stage.revenueContractGroupValue());
        long revenueContractId = groupValueToContractId.computeIfAbsent(
                groupingValue, ignored -> revenueContractHeaderService.nextRevenueContractId());

        revenueContractGroupDetailsService.captureGroupDetail(
                groupingValue, revenueContractId, stage.createdPeriodId(), now, registeredGroupValues, groupDetailsRecords);
        revenueContractReferenceDetailsService.captureReferenceDetail(
                stage, revenueContractId, now, referenceKeys, referenceDetailsRecords);
        stageAssignments.add(new StageRevenueContractAssignment(stage.id(), revenueContractId));
    }

    private static String normalizeGroupingValue(String groupValue) {
        if (groupValue == null || groupValue.isBlank()) {
            return RevenueContractGroupingConstants.DEFAULT_GROUPING_VALUE;
        }
        return groupValue;
    }
}
