package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractOrderAccountDetailsRecord}.
 */
@Component
public final class RevenueContractOrderAccountDetailsRecordMapper implements RowMapper<RevenueContractOrderAccountDetailsRecord> {

    @Override
    public @NonNull RevenueContractOrderAccountDetailsRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var deferSegment1 = rs.getString(c++);
        var deferSegment2 = rs.getString(c++);
        var deferSegment3 = rs.getString(c++);
        var deferSegment4 = rs.getString(c++);
        var deferSegment5 = rs.getString(c++);
        var deferSegment6 = rs.getString(c++);
        var deferSegment7 = rs.getString(c++);
        var deferSegment8 = rs.getString(c++);
        var deferSegment9 = rs.getString(c++);
        var deferSegment10 = rs.getString(c++);
        var revenueSegment1 = rs.getString(c++);
        var revenueSegment2 = rs.getString(c++);
        var revenueSegment3 = rs.getString(c++);
        var revenueSegment4 = rs.getString(c++);
        var revenueSegment5 = rs.getString(c++);
        var revenueSegment6 = rs.getString(c++);
        var revenueSegment7 = rs.getString(c++);
        var revenueSegment8 = rs.getString(c++);
        var revenueSegment9 = rs.getString(c++);
        var revenueSegment10 = rs.getString(c++);
        var customSegment1 = rs.getString(c++);
        var customSegment2 = rs.getString(c++);
        var customSegment3 = rs.getString(c++);
        var customSegment4 = rs.getString(c++);
        var customSegment5 = rs.getString(c++);
        var customSegment6 = rs.getString(c++);
        var customSegment7 = rs.getString(c++);
        var customSegment8 = rs.getString(c++);
        var customSegment9 = rs.getString(c++);
        var customSegment10 = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var updatedPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new RevenueContractOrderAccountDetailsRecord(
                id,
                revenueContractId,
                deferSegment1,
                deferSegment2,
                deferSegment3,
                deferSegment4,
                deferSegment5,
                deferSegment6,
                deferSegment7,
                deferSegment8,
                deferSegment9,
                deferSegment10,
                revenueSegment1,
                revenueSegment2,
                revenueSegment3,
                revenueSegment4,
                revenueSegment5,
                revenueSegment6,
                revenueSegment7,
                revenueSegment8,
                revenueSegment9,
                revenueSegment10,
                customSegment1,
                customSegment2,
                customSegment3,
                customSegment4,
                customSegment5,
                customSegment6,
                customSegment7,
                customSegment8,
                customSegment9,
                customSegment10,
                createdPeriodId,
                updatedPeriodId,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy
        );
    }
}
