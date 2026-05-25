package com.revrec.engine.common.filter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * Applies metadata filter criteria to in-memory record collections.
 *
 * <p>Used by {@code PerformanceObligationRuleFilter}, {@code RevenueContractGroupingFilter},
 * and {@code RevenueContractGroupingTemplateFilter} modules.
 */
@Service
public class RecordFilterService {

    public <T> List<T> filter(Collection<T> records, Collection<? extends FieldFilter> filters) {
        return filter(records, filters, FilterCombination.AND);
    }

    public <T> List<T> filter(
            Collection<T> records, Collection<? extends FieldFilter> filters, FilterCombination combination) {
        Objects.requireNonNull(records, "records");
        Objects.requireNonNull(filters, "filters");
        Objects.requireNonNull(combination, "combination");

        List<? extends FieldFilter> activeFilters =
                filters.stream().filter(FieldFilter::isFilterActive).toList();
        if (activeFilters.isEmpty()) {
            return List.copyOf(records);
        }

        return records.stream().filter(record -> matches(record, activeFilters, combination)).toList();
    }

    private static <T> boolean matches(T record, List<? extends FieldFilter> filters, FilterCombination combination) {
        return switch (combination) {
            case AND -> filters.stream().allMatch(filter -> matches(record, filter));
            case OR -> filters.stream().anyMatch(filter -> matches(record, filter));
        };
    }

    private static <T> boolean matches(T record, FieldFilter filter) {
        Object fieldValue = RecordFieldAccessor.readField(record, filter.filterFieldName());
        return FilterEvaluator.matches(fieldValue, filter);
    }
}
