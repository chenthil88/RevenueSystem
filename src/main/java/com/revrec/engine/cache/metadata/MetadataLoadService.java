package com.revrec.engine.cache.metadata;

import com.revrec.engine.common.metadataservice.Calendar.CalendarService;
import com.revrec.engine.common.metadataservice.Currency.CurrencyService;
import com.revrec.engine.common.metadataservice.CurrentOpenPeriod.CurrentOpenPeriodService;
import com.revrec.engine.common.metadataservice.JournalAccountsSetup.JournalAccountsSetupService;
import com.revrec.engine.common.metadataservice.PerformanceObligationRule.PerformanceObligationRuleService;
import com.revrec.engine.common.metadataservice.PerformanceObligationRuleFilter.PerformanceObligationRuleFilterService;
import com.revrec.engine.common.metadataservice.PerformanceObligationTemplate.PerformanceObligationTemplateService;
import com.revrec.engine.common.metadataservice.RevenueContractGroupingFilter.RevenueContractGroupingFilterService;
import com.revrec.engine.common.metadataservice.RevenueContractGroupingHierarchy.RevenueContractGroupingHierarchyService;
import com.revrec.engine.common.metadataservice.RevenueContractGroupingTemplate.RevenueContractGroupingTemplateService;
import com.revrec.engine.common.metadataservice.StandaloneSellPriceBatchDetails.StandaloneSellPriceBatchDetailsService;
import com.revrec.engine.common.metadataservice.StandaloneSellPriceBatchHeader.StandaloneSellPriceBatchHeaderService;
import com.revrec.engine.common.metadataservice.StandaloneSellPriceHierarchy.StandaloneSellPriceHierarchyService;
import com.revrec.engine.common.metadataservice.StandaloneSellPriceTemplate.StandaloneSellPriceTemplateService;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Loads all metadata tables into Redis cache for current tenant.
 * Orchestrates bulk loading from all 14 metadata services.
 */
@Service
public class MetadataLoadService {

    private static final Logger log = LoggerFactory.getLogger(MetadataLoadService.class);
    private static final int BATCH_SIZE = Integer.MAX_VALUE;

    private final MetadataCache metadataCache;
    private final CalendarService calendarService;
    private final CurrencyService currencyService;
    private final CurrentOpenPeriodService currentOpenPeriodService;
    private final JournalAccountsSetupService journalAccountsSetupService;
    private final PerformanceObligationRuleService performanceObligationRuleService;
    private final PerformanceObligationRuleFilterService performanceObligationRuleFilterService;
    private final PerformanceObligationTemplateService performanceObligationTemplateService;
    private final RevenueContractGroupingFilterService revenueContractGroupingFilterService;
    private final RevenueContractGroupingHierarchyService revenueContractGroupingHierarchyService;
    private final RevenueContractGroupingTemplateService revenueContractGroupingTemplateService;
    private final StandaloneSellPriceBatchDetailsService standaloneSellPriceBatchDetailsService;
    private final StandaloneSellPriceBatchHeaderService standaloneSellPriceBatchHeaderService;
    private final StandaloneSellPriceHierarchyService standaloneSellPriceHierarchyService;
    private final StandaloneSellPriceTemplateService standaloneSellPriceTemplateService;

    public MetadataLoadService(
            MetadataCache metadataCache,
            CalendarService calendarService,
            CurrencyService currencyService,
            CurrentOpenPeriodService currentOpenPeriodService,
            JournalAccountsSetupService journalAccountsSetupService,
            PerformanceObligationRuleService performanceObligationRuleService,
            PerformanceObligationRuleFilterService performanceObligationRuleFilterService,
            PerformanceObligationTemplateService performanceObligationTemplateService,
            RevenueContractGroupingFilterService revenueContractGroupingFilterService,
            RevenueContractGroupingHierarchyService revenueContractGroupingHierarchyService,
            RevenueContractGroupingTemplateService revenueContractGroupingTemplateService,
            StandaloneSellPriceBatchDetailsService standaloneSellPriceBatchDetailsService,
            StandaloneSellPriceBatchHeaderService standaloneSellPriceBatchHeaderService,
            StandaloneSellPriceHierarchyService standaloneSellPriceHierarchyService,
            StandaloneSellPriceTemplateService standaloneSellPriceTemplateService) {
        this.metadataCache = metadataCache;
        this.calendarService = calendarService;
        this.currencyService = currencyService;
        this.currentOpenPeriodService = currentOpenPeriodService;
        this.journalAccountsSetupService = journalAccountsSetupService;
        this.performanceObligationRuleService = performanceObligationRuleService;
        this.performanceObligationRuleFilterService = performanceObligationRuleFilterService;
        this.performanceObligationTemplateService = performanceObligationTemplateService;
        this.revenueContractGroupingFilterService = revenueContractGroupingFilterService;
        this.revenueContractGroupingHierarchyService = revenueContractGroupingHierarchyService;
        this.revenueContractGroupingTemplateService = revenueContractGroupingTemplateService;
        this.standaloneSellPriceBatchDetailsService = standaloneSellPriceBatchDetailsService;
        this.standaloneSellPriceBatchHeaderService = standaloneSellPriceBatchHeaderService;
        this.standaloneSellPriceHierarchyService = standaloneSellPriceHierarchyService;
        this.standaloneSellPriceTemplateService = standaloneSellPriceTemplateService;
    }

    /**
     * Loads all metadata for current tenant into Redis cache.
     *
     * @return load result with count and duration
     */
    public MetadataLoadResult loadMetadata() {
        Instant startTime = Instant.now();
        String tenantId = TenantContext.getTenantId();
        int totalCached = 0;

        try {
            log.info("Starting metadata load for tenant: {}", tenantId);

            totalCached += cacheTable("Calendar", calendarService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("Currency", currencyService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("CurrentOpenPeriod", currentOpenPeriodService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("JournalAccountsSetup", journalAccountsSetupService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("PerformanceObligationRule", performanceObligationRuleService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("PerformanceObligationRuleFilter", performanceObligationRuleFilterService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("PerformanceObligationTemplate", performanceObligationTemplateService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("RevenueContractGroupingFilter", revenueContractGroupingFilterService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("RevenueContractGroupingHierarchy", revenueContractGroupingHierarchyService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("RevenueContractGroupingTemplate", revenueContractGroupingTemplateService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("StandaloneSellPriceBatchDetails", standaloneSellPriceBatchDetailsService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("StandaloneSellPriceBatchHeader", standaloneSellPriceBatchHeaderService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("StandaloneSellPriceHierarchy", standaloneSellPriceHierarchyService.findAll(BATCH_SIZE, 0), tenantId);
            totalCached += cacheTable("StandaloneSellPriceTemplate", standaloneSellPriceTemplateService.findAll(BATCH_SIZE, 0), tenantId);

            Instant endTime = Instant.now();
            long durationMs = java.time.temporal.ChronoUnit.MILLIS.between(startTime, endTime);

            log.info("Metadata load completed for tenant {} - cached {} records in {}ms", tenantId, totalCached, durationMs);

            return new MetadataLoadResult(totalCached, durationMs, tenantId);
        } catch (Exception e) {
            log.error("Failed to load metadata for tenant: {}", tenantId, e);
            throw new RuntimeException("Metadata load failed for tenant: " + tenantId, e);
        }
    }

    private <T> int cacheTable(String tableName, java.util.List<T> records, String tenantId) {
        String tenantLogId = tenantId;
        log.debug("Caching {} records for table {} and tenant {}", records.size(), tableName, tenantLogId);

        int count = 0;
        for (T record : records) {
            try {
                String id = extractId(record);
                metadataCache.put(tableName, tenantId, id, record);
                count++;
            } catch (Exception e) {
                log.warn("Failed to cache record for table {}", tableName, e);
            }
        }
        return count;
    }

    private String extractId(Object record) {
        try {
            var method = record.getClass().getMethod("id");
            Object id = method.invoke(record);
            return String.valueOf(id);
        } catch (Exception e) {
            try {
                var method = record.getClass().getMethod("periodId");
                Object id = method.invoke(record);
                return String.valueOf(id);
            } catch (Exception ex) {
                throw new RuntimeException("Cannot extract ID from record: " + record.getClass().getName(), ex);
            }
        }
    }

    public static class MetadataLoadResult {
        public final int cached;
        public final long durationMs;
        public final String tenantId;

        public MetadataLoadResult(int cached, long durationMs, String tenantId) {
            this.cached = cached;
            this.durationMs = durationMs;
            this.tenantId = tenantId;
        }
    }
}
