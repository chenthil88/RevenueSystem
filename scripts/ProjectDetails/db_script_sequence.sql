-- TiDB sequences: <table_snake>_id_seq, START 1000, CACHE 1000. Run after db_script.sql.
-- Example: INSERT INTO `MyTable` (`id`, ...) VALUES (NEXTVAL(my_table_id_seq), ...);

CREATE SEQUENCE IF NOT EXISTS current_open_period_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `CurrentOpenPeriod`.`OpenPeriodId` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS calendar_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `Calendar`.`PeriodId` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS currency_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `Currency`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS performance_obligation_template_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `PerformanceObligationTemplate`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS performance_obligation_rule_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `PerformanceObligationRule`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS performance_obligation_rule_filter_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `PerformanceObligationRuleFilter`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_grouping_template_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `revenueContractGroupingTemplate`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_grouping_hierarchy_id_seq
    START WITH 1000
    INCREMENT BY 10000
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractGroupingHierarchy`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_grouping_filter_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractGroupingFilter`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS standalone_sell_price_hierarchy_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `StandaloneSellPriceHierarchy`.`seq` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS standalone_sell_price_template_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `StandaloneSellPriceTemplate`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS standalone_sell_price_batch_header_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 100000
    CACHE 1000;
-- `StandaloneSellPriceBatchHeader`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS standalone_sell_price_batch_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 1000000
    CACHE 1000;
-- `StandaloneSellPriceBatchDetails`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_batch_header_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 100000
    CACHE 1000;
-- `revenueContractBatchHeader`.`BatchId` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_batch_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 100000
    CACHE 1000;
-- `revenueContractBatchDetails`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContractHeaderIdSeq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 100000
    CACHE 1000;
-- `revenueContractHead`.`revenueContractId` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenue_contract_group_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractGroupDetails`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenue_contract_reference_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractReferenceDetails`.`Id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_pob_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractPobDetails`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_order_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractOrderDetails`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_allocation_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractAllocationDetails`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_order_account_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractOrderAccountDetails`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_order_attributes_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractOrderAttributes`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_billing_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractBillingDetails`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenueContract_billing_account_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `revenueContractBillingAccountDetails`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS journal_accounts_setup_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `JournalAccountsSetup`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revenue_journal_entries_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `RevenueJournalEntries`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS adjustment_journal_entries_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `AllocationJournalEntries`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revrec_stage_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `RevRecStage`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS revrec_stage_history_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 9223372036854775807
    CACHE 1000;
-- `RevRecStageHistory`.`id` (BIGINT)

CREATE SEQUENCE IF NOT EXISTS contract_modification_header_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `contractModificationHeader`.`id` (INT)

CREATE SEQUENCE IF NOT EXISTS contract_modification_details_id_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 10000
    CACHE 1000;
-- `contractModificationDetails`.`id` (INT)
