package com.revrec.engine.query.dialect;

import com.revrec.engine.query.core.DatabaseProfile;
import org.springframework.stereotype.Component;

@Component
public final class DuckDbDialect implements SqlDialect {

    @Override
    public DatabaseProfile profile() {
        return DatabaseProfile.DUCKDB;
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }

    @Override
    public String limitOffsetClause(String limitParam, String offsetParam) {
        return " LIMIT :" + limitParam + " OFFSET :" + offsetParam;
    }
}
