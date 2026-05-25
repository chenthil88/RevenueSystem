package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model;

import com.revrec.engine.domain.service.RevenueContractGroupDetails.RevenueContractGroupDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractReferenceDetails.RevenueContractReferenceDetailsRecord;
import java.util.List;

/** Result of template-driven revenue contract grouping for a batch. */
public record RevenueContractGroupingResult(
        int groupValuesStamped,
        int stagesUpdated,
        List<RevenueContractGroupDetailsRecord> groupDetailsRecords,
        List<RevenueContractReferenceDetailsRecord> referenceDetailsRecords) {}
