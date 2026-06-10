package com.revrec.engine.common.metadataservice.JournalAccount;

import com.revrec.engine.common.accountdetails.AccountDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class JournalAccountService {

    private final NamedParameterJdbcTemplate jdbc;
    private final JournalAccountRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;
    private volatile Map<String, JournalAccountRecord> cachedByName;

    public JournalAccountService(
            NamedParameterJdbcTemplate jdbc,
            JournalAccountRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `name`, `description`, `SegmentPosition1`, `SegmentPosition2`, `SegmentPosition3`, `SegmentPosition4`, `SegmentPosition5`, `SegmentPosition6`, `SegmentPosition7`, `SegmentPosition8`, `SegmentPosition9`, `SegmentPosition10`, `isActive`, `createdAt`, `updatedAt` FROM `JournalAccountsSetup`";

    public Optional<JournalAccountRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }

    public Optional<JournalAccountRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(JournalAccountRecord.TABLE_NAME, String.valueOf(id), JournalAccountRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            JournalAccountRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }

    public List<JournalAccountRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }

    /**
     * Loads all journal account setup rows (metadata; no pagination), keyed by {@code name}.
     */
    public Map<String, JournalAccountRecord> findAllByName() {
        return jdbc.query(SELECT, Map.of(), rowMapper).stream()
                .filter(row -> row.name() != null && !row.name().isBlank())
                .collect(Collectors.toMap(
                        JournalAccountRecord::name,
                        Function.identity(),
                        (existing, duplicate) -> {
                            throw new IllegalStateException(
                                    "Duplicate JournalAccountsSetup name: " + existing.name());
                        },
                        LinkedHashMap::new));
    }

    /**
     * Builds the journal account string for {@code journalAccountType} using setup segment positions.
     */
    public DerivedJournalAccountValue deriveJournalAccountValue(
            JournalAccountType journalAccountType, AccountDetailsRecord accountDetails) {
        return deriveJournalAccountValue(journalAccountType.accountName(), accountDetails);
    }

    /**
     * Builds the journal account string for {@code journalAccountName} using setup segment positions.
     * Each position is either a segment field name (e.g. {@code RevenueSegment1}) read from
     * {@code accountDetails}, or a literal constant (e.g. {@code 200001}).
     */
    public DerivedJournalAccountValue deriveJournalAccountValue(
            String journalAccountName, AccountDetailsRecord accountDetails) {
        JournalAccountRecord setup = requireSetup(journalAccountName);
        return new DerivedJournalAccountValue(
                journalAccountName,
                JournalAccountSegmentResolver.resolve(setup.segmentPosition1(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition2(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition3(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition4(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition5(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition6(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition7(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition8(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition9(), accountDetails),
                JournalAccountSegmentResolver.resolve(setup.segmentPosition10(), accountDetails));
    }

    private JournalAccountRecord requireSetup(String journalAccountName) {
        JournalAccountRecord setup = cachedSetupByName().get(journalAccountName);
        if (setup == null) {
            throw new IllegalArgumentException("JournalAccount not found for name: " + journalAccountName);
        }
        return setup;
    }

    private Map<String, JournalAccountRecord> cachedSetupByName() {
        Map<String, JournalAccountRecord> local = cachedByName;
        if (local == null) {
            synchronized (this) {
                local = cachedByName;
                if (local == null) {
                    local = findAllByName();
                    cachedByName = local;
                }
            }
        }
        return local;
    }
}
