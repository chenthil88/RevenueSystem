package com.revrec.engine.common.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecordFilterServiceTest {

    private RecordFilterService filterService;

    @BeforeEach
    void setUp() {
        filterService = new RecordFilterService();
    }

    @Test
    void filtersByEqualsAndLike() {
        var records = List.of(
                new SampleRecord("C001", "Acme Corp", BigDecimal.valueOf(100)),
                new SampleRecord("C002", "Beta LLC", BigDecimal.valueOf(200)));

        var filters = List.of(
                new FilterCriterion("customerNumber", "=", "C001"),
                new FilterCriterion("customerName", "LIKE", "Acme%"));

        assertThat(filterService.filter(records, filters)).containsExactly(records.get(0));
    }

    @Test
    void filtersByInAndBetween() {
        var records = List.of(
                new SampleRecord("C001", "Acme Corp", BigDecimal.valueOf(100)),
                new SampleRecord("C002", "Beta LLC", BigDecimal.valueOf(250)),
                new SampleRecord("C003", "Gamma Inc", BigDecimal.valueOf(400)));

        var inFilter = List.of(new FilterCriterion("customerNumber", "IN", "C001,C003"));
        assertThat(filterService.filter(records, inFilter))
                .containsExactly(records.get(0), records.get(2));

        var betweenFilter = List.of(new FilterCriterion("amount", "BETWEEN", "150,300"));
        assertThat(filterService.filter(records, betweenFilter)).containsExactly(records.get(1));
    }

    @Test
    void filtersByNullChecksAndSkipsInactiveCriteria() {
        var records = List.of(
                new SampleRecord(null, "Acme Corp", BigDecimal.valueOf(100)),
                new SampleRecord("C002", "Beta LLC", BigDecimal.valueOf(200)));

        var nullFilter = List.of(new FilterCriterion("customerNumber", "IS NULL", ""));
        assertThat(filterService.filter(records, nullFilter)).containsExactly(records.get(0));

        FieldFilter inactiveFilter = new FieldFilter() {
            @Override
            public String filterFieldName() {
                return "customerNumber";
            }

            @Override
            public String filterOperator() {
                return "=";
            }

            @Override
            public String filterValue() {
                return "C002";
            }

            @Override
            public boolean isFilterActive() {
                return false;
            }
        };
        assertThat(filterService.filter(records, List.of(inactiveFilter))).containsExactlyElementsOf(records);
    }

    @Test
    void supportsOrCombination() {
        var records = List.of(
                new SampleRecord("C001", "Acme Corp", BigDecimal.valueOf(100)),
                new SampleRecord("C002", "Beta LLC", BigDecimal.valueOf(200)));
        var filters = List.of(
                new FilterCriterion("customerNumber", "=", "C001"),
                new FilterCriterion("customerNumber", "=", "C002"));

        assertThat(filterService.filter(records, filters, FilterCombination.OR))
                .containsExactlyElementsOf(records);
    }

    private record SampleRecord(String customerNumber, String customerName, BigDecimal amount) {}
}
