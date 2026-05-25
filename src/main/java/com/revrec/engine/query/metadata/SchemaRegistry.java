package com.revrec.engine.query.metadata;

import com.revrec.engine.domain.SchemaTables;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * In-memory schema catalog. Extend via {@link #register(TableMetadata)} or codegen from {@code db_script.sql}.
 */
@Component
public final class SchemaRegistry {

    private final Map<String, TableMetadata> tables = new LinkedHashMap<>();

    public SchemaRegistry() {
        register(TableMetadata.of(
                SchemaTables.REVENUECONTRACTGROUPDETAILS,
                "Id",
                "RevenueContractId",
                "GroupingValue",
                "ClosedFlag",
                "createdPeriodId",
                "IsActive",
                "CreatedAt",
                "UpdatedAt"));
    }

    public void register(TableMetadata table) {
        tables.put(table.tableName(), table);
    }

    public TableMetadata require(String tableName) {
        TableMetadata table = tables.get(tableName);
        if (table == null) {
            throw new IllegalArgumentException("Unknown table in SchemaRegistry: " + tableName);
        }
        return table;
    }
}
