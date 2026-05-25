package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueContractGrouping;

import org.springframework.stereotype.Component;

/**
 * Builds a TiDB/MySQL expression for {@code RevRecStage} grouping from colon-separated column tokens.
 * Each token must name a {@code RevRecStage} column (case-insensitive), e.g. {@code customerName:salesOrderId}.
 */
@Component
public final class GroupingRuleSqlBuilder {

    static final String STAGE_ALIAS = "stg";

    private final RevRecStageColumnCatalog columnCatalog;

    public GroupingRuleSqlBuilder(RevRecStageColumnCatalog columnCatalog) {
        this.columnCatalog = columnCatalog;
    }

    /**
     * @param groupingFields colon-separated RevRecStage column names from {@code GroupingFields}
     * @return SQL fragment such as {@code CONCAT(IFNULL(stg.`customerName`, ''), ':', ...)}
     */
    public String build(String groupingFields) {
        if (groupingFields == null || groupingFields.isBlank()) {
            throw new IllegalArgumentException("groupingFields must not be blank");
        }
        String[] tokens = groupingFields.split(":");
        if (tokens.length == 1) {
            return "IFNULL(" + columnRef(tokens[0].trim()) + ", '')";
        }
        var sql = new StringBuilder("CONCAT(");
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0) {
                sql.append(", ':', ");
            }
            sql.append("IFNULL(").append(columnRef(tokens[i].trim())).append(", '')");
        }
        sql.append(')');
        return sql.toString();
    }

    private String columnRef(String fieldToken) {
        String column = columnCatalog.resolveColumn(fieldToken);
        return STAGE_ALIAS + ".`" + column + "`";
    }
}
