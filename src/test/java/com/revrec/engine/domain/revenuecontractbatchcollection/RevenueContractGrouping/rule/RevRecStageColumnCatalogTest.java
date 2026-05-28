package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.rule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RevRecStageColumnCatalogTest {

    @Test
    void loadsAllColumnsFromClasspathResource() {
        var catalog = new RevRecStageColumnCatalog();
        assertThat(catalog.columnNames()).contains("customerName", "salesOrderId", "CustomField70", "InvoiceId");
        assertThat(catalog.columnNames()).hasSizeGreaterThan(150);
    }

    @Test
    void resolvesPascalCaseColumnFromDdl() {
        var catalog = new RevRecStageColumnCatalog();
        assertThat(catalog.resolveColumn("invoicenumber")).isEqualTo("InvoiceNumber");
    }
}
