package com.revrec.engine.query.repository;

import com.revrec.engine.domain.SchemaTables;
import com.revrec.engine.domain.service.RevenueContractGroupDetails.RevenueContractGroupDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractGroupDetails.RevenueContractGroupDetailsRecordMapper;
import com.revrec.engine.query.QueryEngineService;
import com.revrec.engine.query.core.DataSourceKey;
import com.revrec.engine.query.metadata.SchemaRegistry;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public final class RevenueContractGroupDetailsRepository
        extends AbstractTableRepository<RevenueContractGroupDetailsRecord, Long> {

    public RevenueContractGroupDetailsRepository(
            QueryEngineService queryEngine,
            SchemaRegistry schemaRegistry,
            RevenueContractGroupDetailsRecordMapper rowMapper) {
        super(
                queryEngine,
                schemaRegistry.require(SchemaTables.REVENUECONTRACTGROUPDETAILS),
                DataSourceKey.PRIMARY_OLTP,
                rowMapper,
                "Id");
    }

    public List<RevenueContractGroupDetailsRecord> findByRevenueContractId(Long revenueContractId) {
        return findByColumn("RevenueContractId", revenueContractId);
    }
}
