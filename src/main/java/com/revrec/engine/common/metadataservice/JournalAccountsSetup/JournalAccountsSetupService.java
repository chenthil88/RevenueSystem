package com.revrec.engine.common.metadataservice.JournalAccountsSetup;

import com.revrec.engine.domain.metadataservice.JournalAccountsSetup.JournalAccountsSetupRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.JournalAccountsSetup.JournalAccountsSetupRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class JournalAccountsSetupService {

    private final NamedParameterJdbcTemplate jdbc;
    private final JournalAccountsSetupRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public JournalAccountsSetupService(
            NamedParameterJdbcTemplate jdbc,
            JournalAccountsSetupRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `name`, `description`, `SegmentPosition1`, `SegmentPosition2`, `SegmentPosition3`, `SegmentPosition4`, `SegmentPosition5`, `SegmentPosition6`, `SegmentPosition7`, `SegmentPosition8`, `SegmentPosition9`, `SegmentPosition10`, `isActive`, `createdAt`, `updatedAt` FROM `JournalAccountsSetup`";
    public Optional<JournalAccountsSetupRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<JournalAccountsSetupRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(JournalAccountsSetupRecord.TABLE_NAME, String.valueOf(id), JournalAccountsSetupRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            JournalAccountsSetupRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<JournalAccountsSetupRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
