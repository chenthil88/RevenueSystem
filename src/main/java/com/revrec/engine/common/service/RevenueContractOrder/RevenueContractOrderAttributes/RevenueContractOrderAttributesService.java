package com.revrec.engine.common.service.RevenueContractOrder.RevenueContractOrderAttributes;

import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAttributes.RevenueContractOrderAttributesRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAttributes.RevenueContractOrderAttributesRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueContractOrderAttributesService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueContractOrderAttributesRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueContractOrderAttributesService(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractOrderAttributesRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `revenueContractId`, `CustomField1`, `CustomField2`, `CustomField3`, `CustomField4`, `CustomField5`, `CustomField6`, `CustomField7`, `CustomField8`, `CustomField9`, `CustomField10`, `CustomField11`, `CustomField12`, `CustomField13`, `CustomField14`, `CustomField15`, `CustomField16`, `CustomField17`, `CustomField18`, `CustomField19`, `CustomField20`, `CustomField21`, `CustomField22`, `CustomField23`, `CustomField24`, `CustomField25`, `CustomField26`, `CustomField27`, `CustomField28`, `CustomField29`, `CustomField30`, `CustomField31`, `CustomField32`, `CustomField33`, `CustomField34`, `CustomField35`, `CustomField36`, `CustomField37`, `CustomField38`, `CustomField39`, `CustomField40`, `CustomField41`, `CustomField42`, `CustomField43`, `CustomField44`, `CustomField45`, `CustomField46`, `CustomField47`, `CustomField48`, `CustomField49`, `CustomField50`, `CustomField51`, `CustomField52`, `CustomField53`, `CustomField54`, `CustomField55`, `CustomField56`, `CustomField57`, `CustomField58`, `CustomField59`, `CustomField60`, `CustomField61`, `CustomField62`, `CustomField63`, `CustomField64`, `CustomField65`, `CustomField66`, `CustomField67`, `CustomField68`, `CustomField69`, `CustomField70`, `CustomNumber1`, `CustomNumber2`, `CustomNumber3`, `CustomNumber4`, `CustomNumber5`, `CustomNumber6`, `CustomNumber7`, `CustomNumber8`, `CustomNumber9`, `CustomNumber10`, `CustomDate1`, `CustomDate2`, `CustomDate3`, `CustomDate4`, `CustomDate5`, `CustomDate6`, `CustomDate7`, `CustomDate8`, `CustomDate9`, `CustomDate10`, `createdPeriodId`, `updatedPeriodId`, `createdAt`, `updatedAt`, `CreatedBy`, `UpdatedBy` FROM `revenueContractOrderAttributes`";
    public Optional<RevenueContractOrderAttributesRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueContractOrderAttributesRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueContractOrderAttributesRecord.TABLE_NAME, String.valueOf(id), RevenueContractOrderAttributesRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueContractOrderAttributesRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueContractOrderAttributesRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
