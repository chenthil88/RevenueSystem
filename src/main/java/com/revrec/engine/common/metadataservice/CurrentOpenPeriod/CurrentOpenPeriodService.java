package com.revrec.engine.common.metadataservice.CurrentOpenPeriod;

import com.revrec.engine.domain.metadataservice.CurrentOpenPeriod.CurrentOpenPeriodRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.CurrentOpenPeriod.CurrentOpenPeriodRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class CurrentOpenPeriodService {

    private final NamedParameterJdbcTemplate jdbc;
    private final CurrentOpenPeriodRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public CurrentOpenPeriodService(
            NamedParameterJdbcTemplate jdbc,
            CurrentOpenPeriodRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `OpenPeriodId`, `OpenPeriodName`, `OpenPeriodStartDate`, `OpenPeriodEndDate`, `OpenPeriodStatus`, `OpenPeriodCreatedAt`, `OpenPeriodUpdatedAt`, `OrganizationId`, `BookId` FROM `CurrentOpenPeriod`";
    public Optional<CurrentOpenPeriodRecord> findById(Long openPeriodId) {
        var list = jdbc.query(SELECT + " WHERE `OpenPeriodId` = :id", Map.of("id", openPeriodId), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<CurrentOpenPeriodRecord> findByIdCached(Long openPeriodId) {
        return noSqlRecordServer
                .get(CurrentOpenPeriodRecord.TABLE_NAME, String.valueOf(openPeriodId), CurrentOpenPeriodRecord.class)
                .or(() -> findById(openPeriodId).map(row -> {
                    noSqlRecordServer.put(
                            CurrentOpenPeriodRecord.TABLE_NAME, String.valueOf(row.openPeriodId()), row);
                    return row;
                }));
    }
    public List<CurrentOpenPeriodRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
