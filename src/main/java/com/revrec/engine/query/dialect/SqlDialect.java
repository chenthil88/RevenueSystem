package com.revrec.engine.query.dialect;

import com.revrec.engine.query.core.DatabaseProfile;

/**
 * Database-specific SQL rendering (identifiers, pagination, upsert).
 */
public interface SqlDialect {

    DatabaseProfile profile();

    String quoteIdentifier(String identifier);

    String limitOffsetClause(String limitParam, String offsetParam);
}
