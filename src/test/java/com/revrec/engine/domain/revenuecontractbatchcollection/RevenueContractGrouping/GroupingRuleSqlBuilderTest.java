package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueContractGrouping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupingRuleSqlBuilderTest {

    private GroupingRuleSqlBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new GroupingRuleSqlBuilder(new RevRecStageColumnCatalog(
                "customerName", "salesOrderId", "InvoiceId", "CustomField1"));
    }

    @Test
    void buildsConcatForColonSeparatedColumns() {
        String sql = builder.build("customerName:salesOrderId");
        assertThat(sql)
                .isEqualTo(
                        "CONCAT(IFNULL(stg.`customerName`, ''), ':', IFNULL(stg.`salesOrderId`, ''))");
    }

    @Test
    void resolvesColumnsCaseInsensitively() {
        String sql = builder.build("CustomerName:SalesOrderId");
        assertThat(sql)
                .isEqualTo(
                        "CONCAT(IFNULL(stg.`customerName`, ''), ':', IFNULL(stg.`salesOrderId`, ''))");
    }

    @Test
    void buildsSingleColumnExpression() {
        String sql = builder.build("CustomField1");
        assertThat(sql).isEqualTo("IFNULL(stg.`CustomField1`, '')");
    }

    @Test
    void rejectsUnknownColumn() {
        assertThatThrownBy(() -> builder.build("NotARealColumn"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown RevRecStage column");
    }
}
