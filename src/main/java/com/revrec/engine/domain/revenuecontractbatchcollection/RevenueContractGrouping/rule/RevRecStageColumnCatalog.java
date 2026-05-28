package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.rule;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Allowed {@code RevRecStage} column names (from {@code db_script.sql}), used to validate grouping tokens.
 */
@Component
public final class RevRecStageColumnCatalog {

    private final Map<String, String> byNormalizedKey;
    private final Set<String> columnNames;

    public RevRecStageColumnCatalog() {
        this(loadColumnsFromClasspath());
    }

    RevRecStageColumnCatalog(String... columns) {
        var normalized = new LinkedHashMap<String, String>();
        for (String column : columns) {
            String trimmed = column.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String key = normalize(trimmed);
            normalized.putIfAbsent(key, trimmed);
        }
        this.byNormalizedKey = Map.copyOf(normalized);
        this.columnNames = Set.copyOf(normalized.values());
    }

    public Set<String> columnNames() {
        return columnNames;
    }

    public String resolveColumn(String token) {
        String trimmed = Objects.requireNonNull(token, "token").trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Empty field token in groupingFields");
        }
        String column = byNormalizedKey.get(normalize(trimmed));
        if (column == null) {
            throw new IllegalArgumentException(
                    "Unknown RevRecStage column '" + trimmed + "'. Use a column from RevRecStage (see "
                            + RevenueContractGroupingConstants.REV_REC_STAGE_COLUMNS_RESOURCE + ").");
        }
        return column;
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    private static String[] loadColumnsFromClasspath() {
        var resource = new ClassPathResource(RevenueContractGroupingConstants.REV_REC_STAGE_COLUMNS_RESOURCE);
        try (var reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().filter(line -> !line.isBlank()).toArray(String[]::new);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load " + RevenueContractGroupingConstants.REV_REC_STAGE_COLUMNS_RESOURCE, e);
        }
    }
}
