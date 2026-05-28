package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class AllocationInitialEntryService {

    public void createInitialEntry(
            List<RevenueContractOrderRecords> revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Objects.requireNonNull(revenueContractOrderRecords, "revenueContractOrderRecords");
        Objects.requireNonNull(revenueContractHeaderRecord, "revenueContractHeaderRecord");
        // TODO: implement initial entry creation
    }
}

