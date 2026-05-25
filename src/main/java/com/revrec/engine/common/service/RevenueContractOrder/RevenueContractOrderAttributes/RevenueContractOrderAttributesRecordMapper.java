package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAttributes;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueContractOrderAttributesRecord}.
 */
@Component
public final class RevenueContractOrderAttributesRecordMapper implements RowMapper<RevenueContractOrderAttributesRecord> {

    @Override
    public @NonNull RevenueContractOrderAttributesRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var customField1 = rs.getString(c++);
        var customField2 = rs.getString(c++);
        var customField3 = rs.getString(c++);
        var customField4 = rs.getString(c++);
        var customField5 = rs.getString(c++);
        var customField6 = rs.getString(c++);
        var customField7 = rs.getString(c++);
        var customField8 = rs.getString(c++);
        var customField9 = rs.getString(c++);
        var customField10 = rs.getString(c++);
        var customField11 = rs.getString(c++);
        var customField12 = rs.getString(c++);
        var customField13 = rs.getString(c++);
        var customField14 = rs.getString(c++);
        var customField15 = rs.getString(c++);
        var customField16 = rs.getString(c++);
        var customField17 = rs.getString(c++);
        var customField18 = rs.getString(c++);
        var customField19 = rs.getString(c++);
        var customField20 = rs.getString(c++);
        var customField21 = rs.getString(c++);
        var customField22 = rs.getString(c++);
        var customField23 = rs.getString(c++);
        var customField24 = rs.getString(c++);
        var customField25 = rs.getString(c++);
        var customField26 = rs.getString(c++);
        var customField27 = rs.getString(c++);
        var customField28 = rs.getString(c++);
        var customField29 = rs.getString(c++);
        var customField30 = rs.getString(c++);
        var customField31 = rs.getString(c++);
        var customField32 = rs.getString(c++);
        var customField33 = rs.getString(c++);
        var customField34 = rs.getString(c++);
        var customField35 = rs.getString(c++);
        var customField36 = rs.getString(c++);
        var customField37 = rs.getString(c++);
        var customField38 = rs.getString(c++);
        var customField39 = rs.getString(c++);
        var customField40 = rs.getString(c++);
        var customField41 = rs.getString(c++);
        var customField42 = rs.getString(c++);
        var customField43 = rs.getString(c++);
        var customField44 = rs.getString(c++);
        var customField45 = rs.getString(c++);
        var customField46 = rs.getString(c++);
        var customField47 = rs.getString(c++);
        var customField48 = rs.getString(c++);
        var customField49 = rs.getString(c++);
        var customField50 = rs.getString(c++);
        var customField51 = rs.getString(c++);
        var customField52 = rs.getString(c++);
        var customField53 = rs.getString(c++);
        var customField54 = rs.getString(c++);
        var customField55 = rs.getString(c++);
        var customField56 = rs.getString(c++);
        var customField57 = rs.getString(c++);
        var customField58 = rs.getString(c++);
        var customField59 = rs.getString(c++);
        var customField60 = rs.getString(c++);
        var customField61 = rs.getString(c++);
        var customField62 = rs.getString(c++);
        var customField63 = rs.getString(c++);
        var customField64 = rs.getString(c++);
        var customField65 = rs.getString(c++);
        var customField66 = rs.getString(c++);
        var customField67 = rs.getString(c++);
        var customField68 = rs.getString(c++);
        var customField69 = rs.getString(c++);
        var customField70 = rs.getString(c++);
        var customNumber1 = rs.getBigDecimal(c++);
        var customNumber2 = rs.getBigDecimal(c++);
        var customNumber3 = rs.getBigDecimal(c++);
        var customNumber4 = rs.getBigDecimal(c++);
        var customNumber5 = rs.getBigDecimal(c++);
        var customNumber6 = rs.getBigDecimal(c++);
        var customNumber7 = rs.getBigDecimal(c++);
        var customNumber8 = rs.getBigDecimal(c++);
        var customNumber9 = rs.getBigDecimal(c++);
        var customNumber10 = rs.getBigDecimal(c++);
        var customDate1 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate2 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate3 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate4 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate5 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate6 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate7 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate8 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate9 = rs.getObject(c++, java.time.LocalDate.class);
        var customDate10 = rs.getObject(c++, java.time.LocalDate.class);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var updatedPeriodId = rs.getObject(c++, Long.class);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        return new RevenueContractOrderAttributesRecord(
                id,
                revenueContractId,
                customField1,
                customField2,
                customField3,
                customField4,
                customField5,
                customField6,
                customField7,
                customField8,
                customField9,
                customField10,
                customField11,
                customField12,
                customField13,
                customField14,
                customField15,
                customField16,
                customField17,
                customField18,
                customField19,
                customField20,
                customField21,
                customField22,
                customField23,
                customField24,
                customField25,
                customField26,
                customField27,
                customField28,
                customField29,
                customField30,
                customField31,
                customField32,
                customField33,
                customField34,
                customField35,
                customField36,
                customField37,
                customField38,
                customField39,
                customField40,
                customField41,
                customField42,
                customField43,
                customField44,
                customField45,
                customField46,
                customField47,
                customField48,
                customField49,
                customField50,
                customField51,
                customField52,
                customField53,
                customField54,
                customField55,
                customField56,
                customField57,
                customField58,
                customField59,
                customField60,
                customField61,
                customField62,
                customField63,
                customField64,
                customField65,
                customField66,
                customField67,
                customField68,
                customField69,
                customField70,
                customNumber1,
                customNumber2,
                customNumber3,
                customNumber4,
                customNumber5,
                customNumber6,
                customNumber7,
                customNumber8,
                customNumber9,
                customNumber10,
                customDate1,
                customDate2,
                customDate3,
                customDate4,
                customDate5,
                customDate6,
                customDate7,
                customDate8,
                customDate9,
                customDate10,
                createdPeriodId,
                updatedPeriodId,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy
        );
    }
}
