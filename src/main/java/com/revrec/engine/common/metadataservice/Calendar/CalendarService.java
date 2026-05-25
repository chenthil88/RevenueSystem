package com.revrec.engine.common.metadataservice.Calendar;

import com.revrec.engine.domain.metadataservice.Calendar.CalendarRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.metadataservice.Calendar.CalendarRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class CalendarService {

    private final NamedParameterJdbcTemplate jdbc;
    private final CalendarRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public CalendarService(
            NamedParameterJdbcTemplate jdbc,
            CalendarRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `PeriodId`, `PeriodName`, `QuarterStartDate`, `QuarterEndDate`, `MonthStartDate`, `MonthEndDate`, `YearStartDate`, `YearEndDate`, `CreatedAt`, `UpdatedAt` FROM `Calendar`";
    public Optional<CalendarRecord> findById(Long periodId) {
        var list = jdbc.query(SELECT + " WHERE `PeriodId` = :id", Map.of("id", periodId), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<CalendarRecord> findByIdCached(Long periodId) {
        return noSqlRecordServer
                .get(CalendarRecord.TABLE_NAME, String.valueOf(periodId), CalendarRecord.class)
                .or(() -> findById(periodId).map(row -> {
                    noSqlRecordServer.put(
                            CalendarRecord.TABLE_NAME, String.valueOf(row.periodId()), row);
                    return row;
                }));
    }
    public List<CalendarRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
