package com.revrec.engine.common.filter;

import static org.assertj.core.api.Assertions.assertThat;

import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails.RevenueContractOrderDetailsRecord;
import java.util.List;
import org.junit.jupiter.api.Test;

class RevenueContractOrderFilterTest {

    private final RecordFilterService filterService = new RecordFilterService();

    @Test
    void appliesMultipleFiltersToRevenueContractOrderRecords() {
        var matching = orderRecord("C001", "Acme Corp");
        var wrongCustomer = orderRecord("C002", "Acme Corp");
        var wrongName = orderRecord("C001", "Beta LLC");
        var records = List.of(matching, wrongCustomer, wrongName);

        var filters = List.of(
                new FilterCriterion("customerNumber", "=", "C001"),
                new FilterCriterion("customerName", "LIKE", "Acme%"));

        assertThat(filterService.filter(records, filters)).containsExactly(matching);
    }

    private static RevenueContractOrderDetailsRecord orderRecord(String customerNumber, String customerName) {
        return new RevenueContractOrderDetailsRecord(
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                customerNumber,
                customerName,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
