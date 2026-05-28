package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.rule;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import org.springframework.stereotype.Component;

/**
 * Builds a TiDB/MySQL expression for {@code RevRecStage} grouping from colon-separated column tokens.
 */
@Component
public final class GroupingRuleSqlBuilder {

    private final RevRecStageColumnCatalog columnCatalog;

    public GroupingRuleSqlBuilder(RevRecStageColumnCatalog columnCatalog) {
        this.columnCatalog = columnCatalog;
    }

    public String build(String groupingFields) {
        if (groupingFields == null || groupingFields.isBlank()) {
            throw new IllegalArgumentException("groupingFields must not be blank");
        }
        String[] tokens = groupingFields.split(RevenueContractGroupingConstants.GROUPING_FIELDS_SEPARATOR);
        if (tokens.length == 1) {
            return "IFNULL(" + columnRef(tokens[0].trim()) + ", '')";
        }
        var sql = new StringBuilder("CONCAT(");
        String separator = RevenueContractGroupingConstants.GROUPING_VALUE_SEPARATOR;
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0) {
                sql.append(", '").append(separator).append("', ");
            }
            sql.append("IFNULL(").append(columnRef(tokens[i].trim())).append(", '')");
        }
        sql.append(')');
        return sql.toString();
    }

    private String columnRef(String fieldToken) {
        String column = columnCatalog.resolveColumn(fieldToken);
        return RevenueContractGroupingConstants.REV_REC_STAGE_ALIAS + ".`" + column + "`";
    }
}
