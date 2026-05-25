package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueContractGrouping;

import com.revrec.engine.domain.service.RevenueContractGroupDetails.RevenueContractGroupDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractReferenceDetails.RevenueContractReferenceDetailsRecord;
import java.util.List;

public record RevenueContractGroupingResult(
        int groupValuesStamped,
        int stagesUpdated,
        List<RevenueContractGroupDetailsRecord> groupDetailsRecords,
        List<RevenueContractReferenceDetailsRecord> referenceDetailsRecords) {}
