package com.revrec.engine.query.router;

import com.revrec.engine.query.core.DataSourceKey;
import com.revrec.engine.query.core.DatabaseProfile;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Resolves JDBC templates and dialect profiles per logical data source.
 */
@Component
public final class ConnectionRouter {

    private final Map<DataSourceKey, NamedParameterJdbcTemplate> jdbcByKey;
    private final Map<DataSourceKey, DatabaseProfile> profileByKey;

    public ConnectionRouter(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            NamedParameterJdbcTemplate batchDuckNamedJdbcTemplate) {
        var jdbc = new EnumMap<DataSourceKey, NamedParameterJdbcTemplate>(DataSourceKey.class);
        jdbc.put(DataSourceKey.PRIMARY_OLTP, namedParameterJdbcTemplate);
        jdbc.put(DataSourceKey.BATCH_TEMP, batchDuckNamedJdbcTemplate);

        var profiles = new EnumMap<DataSourceKey, DatabaseProfile>(DataSourceKey.class);
        profiles.put(DataSourceKey.PRIMARY_OLTP, DatabaseProfile.MYSQL);
        profiles.put(DataSourceKey.BATCH_TEMP, DatabaseProfile.DUCKDB);

        this.jdbcByKey = Map.copyOf(jdbc);
        this.profileByKey = Map.copyOf(profiles);
    }

    public NamedParameterJdbcTemplate jdbc(DataSourceKey key) {
        NamedParameterJdbcTemplate jdbc = jdbcByKey.get(key);
        if (jdbc == null) {
            throw new IllegalArgumentException("No JDBC template for DataSourceKey: " + key);
        }
        return jdbc;
    }

    public DatabaseProfile profile(DataSourceKey key) {
        DatabaseProfile profile = profileByKey.get(key);
        if (profile == null) {
            throw new IllegalArgumentException("No DatabaseProfile for DataSourceKey: " + key);
        }
        return profile;
    }
}
