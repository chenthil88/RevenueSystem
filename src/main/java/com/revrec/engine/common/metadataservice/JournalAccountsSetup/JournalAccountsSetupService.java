package com.revrec.engine.common.metadataservice.JournalAccountsSetup;

import com.revrec.engine.domain.metadataservice.JournalAccountsSetup.JournalAccountsSetupRecord;
import com.revrec.engine.common.accountdetails.AccountDetailsRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.JournalAccountsSetup.JournalAccountsSetupRecordMapper;
import java.util.LinkedHashMap;
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
public class JournalAccountsSetupService {

    private final NamedParameterJdbcTemplate jdbc;
    private final JournalAccountsSetupRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;
    private volatile Map<String, JournalAccountsSetupRecord> cachedByName;

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
    /**
     * Loads all journal account setup rows (metadata; no pagination), keyed by {@code name}.
     */
    public Map<String, JournalAccountsSetupRecord> findAllByName() {
        return jdbc.query(SELECT, Map.of(), rowMapper).stream()
                .filter(row -> row.name() != null && !row.name().isBlank())
                .collect(Collectors.toMap(
                        JournalAccountsSetupRecord::name,
                        Function.identity(),
                        (existing, duplicate) -> {
                            throw new IllegalStateException(
                                    "Duplicate JournalAccountsSetup name: " + existing.name());
                        },
                        LinkedHashMap::new));
    }

    /**
     * Builds the journal account string for {@code journalAccountName} using setup segment positions.
     * Each position is either a segment field name (e.g. {@code RevenueSegment1}) read from
     * {@code accountDetails}, or a literal constant (e.g. {@code 200001}).
     * <p>
     * Combined value: {@link DerivedJournalAccountValue#delimitedAccountValue()} using
     * {@link DerivedJournalAccountValue#DELIMITER} ({@code |}). Split with
     * {@link DerivedJournalAccountValue#splitDelimitedAccountValue()} or map
     * {@link DerivedJournalAccountValue#segment(int)} (1–10) into separate columns.
     *
     * @param accountDetails order or billing account-details row implementing {@link AccountDetailsRecord}
     */
    public DerivedJournalAccountValue deriveJournalAccountValue(
            String journalAccountName, AccountDetailsRecord accountDetails) {
        JournalAccountsSetupRecord setup = requireSetup(journalAccountName);
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

    private JournalAccountsSetupRecord requireSetup(String journalAccountName) {
        JournalAccountsSetupRecord setup = cachedSetupByName().get(journalAccountName);
        if (setup == null) {
            throw new IllegalArgumentException("JournalAccountsSetup not found for name: " + journalAccountName);
        }
        return setup;
    }

    private Map<String, JournalAccountsSetupRecord> cachedSetupByName() {
        Map<String, JournalAccountsSetupRecord> local = cachedByName;
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
