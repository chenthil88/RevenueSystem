package com.revrec.engine.query.builder;

import static org.assertj.core.api.Assertions.assertThat;

import com.revrec.engine.query.core.DatabaseProfile;
import com.revrec.engine.query.dialect.DuckDbDialect;
import com.revrec.engine.query.dialect.DialectRegistry;
import com.revrec.engine.query.dialect.MySqlDialect;
import com.revrec.engine.query.metadata.TableMetadata;
import com.revrec.engine.query.spec.QueryPredicate;
import com.revrec.engine.query.spec.QuerySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryBuilderTest {

    private QueryBuilder queryBuilder;
    private TableMetadata table;

    @BeforeEach
    void setUp() {
        queryBuilder = new QueryBuilder(new DialectRegistry(java.util.List.of(new MySqlDialect(), new DuckDbDialect())));
        table = TableMetadata.of(
                "revenueContractGroupDetails",
                "Id",
                "RevenueContractId",
                "GroupingValue");
    }

    @Test
    void compilesMysqlSelectWithWhere() {
        var spec = QuerySpec.forTable(table).where(new QueryPredicate.Eq("Id", 42L));
        var compiled = queryBuilder.compile(spec, DatabaseProfile.MYSQL);

        assertThat(compiled.sql())
                .isEqualTo(
                        "SELECT `Id`, `RevenueContractId`, `GroupingValue` FROM `revenueContractGroupDetails` WHERE `Id` = :w_Id");
        assertThat(compiled.parameters()).containsEntry("w_Id", 42L);
    }

    @Test
    void compilesDuckDbWithLimitOffset() {
        var spec = QuerySpec.forTable(table).limit(10).offset(5);
        var compiled = queryBuilder.compile(spec, DatabaseProfile.DUCKDB);

        assertThat(compiled.sql())
                .endsWith(" LIMIT :qe_limit OFFSET :qe_offset");
        assertThat(compiled.sql()).contains("\"revenueContractGroupDetails\"");
        assertThat(compiled.parameters()).containsEntry("qe_limit", 10).containsEntry("qe_offset", 5);
    }
}
