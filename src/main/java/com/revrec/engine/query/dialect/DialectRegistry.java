package com.revrec.engine.query.dialect;

import com.revrec.engine.query.core.DatabaseProfile;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public final class DialectRegistry {

    private final Map<DatabaseProfile, SqlDialect> byProfile;

    public DialectRegistry(List<SqlDialect> dialects) {
        var map = new EnumMap<DatabaseProfile, SqlDialect>(DatabaseProfile.class);
        for (SqlDialect dialect : dialects) {
            map.put(dialect.profile(), dialect);
        }
        this.byProfile = Map.copyOf(map);
    }

    public SqlDialect require(DatabaseProfile profile) {
        SqlDialect dialect = byProfile.get(profile);
        if (dialect == null) {
            throw new IllegalArgumentException("No SqlDialect registered for profile: " + profile);
        }
        return dialect;
    }
}
