INSERT INTO CurrentOpenPeriod (
    OpenPeriodId, 
    OpenPeriodName, 
    OpenPeriodStartDate, 
    OpenPeriodEndDate, 
    OpenPeriodStatus, 
    OpenPeriodCreatedAt, 
    OpenPeriodUpdatedAt, 
    OrganizationId, 
    BookId
) VALUES (
    202601,                     -- ID (example: YYYYMM format)
    'Jan-2026',                 -- Name
    '2026-01-01',               -- Start Date
    '2026-01-31',               -- End Date
    'OPEN',                     -- Status
    NOW(),                      -- CreatedAt (Current timestamp)
    NOW(),                      -- UpdatedAt (Current timestamp)
    1,                          -- OrganizationId (example)
    1                         -- BookId (example)
);


-- Reference / seed data for contractModificationDetails (see db_script.sql).

INSERT INTO contractModificationDetails (
    `id`,
    `RuleName`,
    `RuleTreatmentForDistinctPob`,
    `RuleTreatmentForNonDistinctPob`,
    `ruleCategory`,
    `IsActive`,
    `createdPeriodId`,
    `updatedPeriodId`,
    `createdAt`,
    `updatedAt`,
    `createdBy`
) VALUES
    (1, 'Quantity Increase', 'Prospective', 'Retrospective', 'Order Modification', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (2, 'Quantity Decrease', 'Prospective', 'Retrospective', 'Order Modification', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (3, 'Term Increase', 'Prospective', 'Retrospective', 'Order Modification', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (4, 'Term Decrease', 'Prospective', 'Retrospective', 'Order Modification', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (5, 'Price Increase', 'Prospective', 'Retrospective', 'Order Modification', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (6, 'Price Decrease', 'Prospective', 'Retrospective', 'Order Modification', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (7, 'Cancel Order', 'Prospective', 'Retrospective', 'Cancel Order', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (8, 'Credit Memo Cancel Posted Revenue', 'Prospective', 'Retrospective', 'Credit Memo', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (9, 'Credit Memo Cancel Future Revenue', 'Prospective', 'Retrospective', 'Credit Memo', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (10, 'Return Order Cancel Future Revenue', 'Prospective', 'Retrospective', 'Return Order', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (11, 'Return Order Cancel Posted Revenue', 'Prospective', 'Retrospective', 'Return Order', TRUE, 202601, 202601, NOW(), NOW(), 'system'),
    (12, 'Adding New Line To Existing Contract', 'Prospective', 'Retrospective', 'New Line', TRUE, 202601, 202601, NOW(), NOW(), 'system');


INSERT INTO contractModificationHeader (
    `id`,
    `InitialContractModificationDuration`,
    `RevisionContractModificationDuration`,
    `InitialContractModificationSspDateMethod`,
    `RevisionContractModificationSspDateMethod`,
    `createdPeriodId`,
    `updatedPeriodId`,
    `createdAt`,
    `updatedAt`,
    `createdBy`,
    `updatedBy`
) VALUES (
    1,
    'Current Accounting Period End date',
    'Next Accounting Period End Date',
    'Minium of Arrangement Sales order Book date',
    'Sales Order Date',
    202601,
    202601,
    NOW(),
    NOW(),
    'system',
    'system'
);


-- Seed data for PerformanceObligationTemplate (see db_script.sql).

INSERT INTO PerformanceObligationTemplate (
    `Id`,
    `Name`,
    `Description`,
    `RevenueReleaseMethod`,
    `RevenueReleaseTiming`,
    `RevenueCalculationMethod`,
    `IsDistinctPob`,
    `IsActive`,
    `createdPeriodId`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES
    (1, 'Software License', 'Software License', 'Point In Time', 'Order', 'Daily', TRUE, TRUE, 202601, NOW(), NOW()),
    (2, 'Service', 'Service', 'Ratable', 'Order', 'Monthly', TRUE, TRUE, 202601, NOW(), NOW()),
    (3, 'Maintenance', 'Maintenance', 'Contract Ratable', 'Billing', 'Monthly', TRUE, TRUE, 202601, NOW(), NOW());



INSERT INTO PerformanceObligationRule(
    Id,
    Name,
    Description,
    PerformanceObligationTemplateId,
    IsActive,
    createdPeriodId,
    CreatedAt,
    UpdatedAt
) VALUES (1, 'Software License1', 'Software License1', 1, TRUE, 202601, NOW(), NOW());


INSERT INTO PerformanceObligationRuleFilter (
    `Id`,
    `PerformanceObligationRuleId`,
    `FilterFieldName`,
    `FilterOperator`,
    `FilterValue`,
    `IsActive`,
    `createdPeriodId`,
    `CreatedAt`,
    `UpdatedAt`,
    `CreatedBy`,
    `UpdatedBy`
) VALUES (
    1,
    1,
    'ProductFamily',
    '=',
    'Hosted Applications',
    TRUE,
    202601,
    NOW(),
    NOW(),
    'system',
    'system'
);


-- -----------------------------------------------------------------------------
-- Auto-generated seed INSERTs from db_script.sql (tables without prior INSERT in Data.sql)
-- Review NULLs and FK ids (1 / 202601) before applying to a real database.
-- -----------------------------------------------------------------------------

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202601,
    'Jan-2026',
    '2026-01-01',
    '2026-03-31',
    '2026-01-01',
    '2026-01-31',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()
);

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202602,
    'Feb-2026',
    '2026-01-01',
    '2026-03-31',
    '2026-02-01',
    '2026-02-28',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()
);

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202603,
    'Mar-2026',
    '2026-01-01',
    '2026-03-31',
    '2026-01-01',
    '2026-01-31',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()
);

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202604,
    'Apr-2026',
    '2026-04-01',
    '2026-06-30',
    '2026-04-01',
    '2026-04-30',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()
);

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202605,
    'May-2026',
    '2026-04-01',
    '2026-06-30',
    '2026-05-01',
    '2026-05-31',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()
);

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202606,
    'Jun-2026',
    '2026-04-01',
    '2026-06-30',
    '2026-06-01',
    '2026-06-30',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()
);

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202607,
    'Jul-2026',
    '2026-07-01',
    '2026-09-30',
    '2026-07-01',
    '2026-07-31',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()   
);

INSERT INTO `Calendar` (
    `PeriodId`,
    `PeriodName`,
    `QuarterStartDate`,
    `QuarterEndDate`,
    `MonthStartDate`,
    `MonthEndDate`,
    `YearStartDate`,
    `YearEndDate`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    202608,
    'Aug-2026',
    '2026-07-01',
    '2026-09-30',
    '2026-08-01',
    '2026-08-31',
    '2026-01-01',
    '2026-12-31',
    NOW(),
    NOW()   
);

INSERT INTO `Calendar` (
        `PeriodId`,
        `PeriodName`,
        `QuarterStartDate`,
        `QuarterEndDate`,
        `MonthStartDate`,
        `MonthEndDate`,
        `YearStartDate`,
        `YearEndDate`,
        `CreatedAt`,
        `UpdatedAt`
    ) VALUES (
        202609,
        'Sep-2026',
        '2026-09-01',
        '2026-11-30',
        '2026-09-01',
        '2026-09-30',
        '2026-01-01',
        '2026-12-31',
        NOW(),
        NOW()
    );

    INSERT INTO `Calendar` (
        `PeriodId`,
        `PeriodName`,
        `QuarterStartDate`,
        `QuarterEndDate`,
        `MonthStartDate`,
        `MonthEndDate`,
        `YearStartDate`,
        `YearEndDate`,
        `CreatedAt`,
        `UpdatedAt`
    ) VALUES (
        202610,
        'Oct-2026',
        '2026-09-01',
        '2026-11-30',
        '2026-10-01',
        '2026-10-31',
        '2026-01-01',
        '2026-12-31',
        NOW(),
        NOW()
    );

    INSERT INTO `Calendar` (
        `PeriodId`,
        `PeriodName`,
        `QuarterStartDate`,
        `QuarterEndDate`,
        `MonthStartDate`,
        `MonthEndDate`,
        `YearStartDate`,
        `YearEndDate`,
        `CreatedAt`,
        `UpdatedAt`
    ) VALUES (
        202611,
        'Nov-2026',
        '2026-09-01',
        '2026-12-31',
        '2026-11-01',
        '2026-11-30',
        '2026-01-01',
        '2026-12-31',
        NOW(),
        NOW()
    );

    INSERT INTO `Calendar` (
        `PeriodId`,
        `PeriodName`,
        `QuarterStartDate`,
        `QuarterEndDate`,
        `MonthStartDate`,
        `MonthEndDate`,
        `YearStartDate`,
        `YearEndDate`,
        `CreatedAt`,
        `UpdatedAt`
    ) VALUES (  
        202612,
        'Dec-2026',
        '2026-09-01',
        '2026-12-31',
        '2026-12-01',
        '2026-12-31',
        '2026-01-01',
        '2026-12-31',
        NOW(),
        NOW()
    );

INSERT INTO `Currency` (
    `Id`,
    `CurrencyName`,
    `CurrencyCode`,
    `CurrencyRounding`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    1,
    'US Dollar',
    'USD',
    2,
    NOW(),
    NOW()
);

INSERT INTO `Currency` (
    `Id`,
    `CurrencyName`,
    `CurrencyCode`,
    `CurrencyRounding`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    2,
    'Japanese Yen',
    'JPY',
    0,
    NOW(),
    NOW()
);

INSERT INTO `Currency` (
    `Id`,
    `CurrencyName`,
    `CurrencyCode`,
    `CurrencyRounding`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    3,
    'Euro',
    'EUR',
    2,
    NOW(),
    NOW()
);

INSERT INTO `ArrangementGroupingTemplate` (
    `Id`,
    `Name`,
    `Description`,
    `IsActive`,
    `createdPeriodId`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    1,
    'ChargeBee Arrangement Grouping',
    'ChargeBee Arrangement Grouping',
    TRUE,
    202601,
    NOW(),
    NOW()
);

INSERT INTO `ArrangementGroupingHierarchy` (
    `Id`,
    `sequence`,
    `ArrangementGroupingTemplateId`,
    `GroupingFields`,
    `createdPeriodId`,
    `IsActive`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    1,
    1,
    1,
    'CustomField1',
    202601,
    TRUE,
    NOW(),
    NOW()
);

INSERT INTO `ArrangementGroupingHierarchy` (
    `Id`,
    `sequence`,
    `ArrangementGroupingTemplateId`,
    `GroupingFields`,
    `createdPeriodId`,
    `IsActive`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    2,
    2,
    1,
    'SalesOrderNumber',
    202601,
    TRUE,
    NOW(),
    NOW()
);

INSERT INTO `ArrangementGroupingFilter` (
    `Id`,
    `ArrangementGroupingTemplateId`,
    `FilterFieldName`,
    `FilterOperator`,
    `FilterValue`,
    `IsActive`,
    `createdPeriodId`,
    `CreatedAt`,
    `UpdatedAt`,
    `CreatedBy`,
    `UpdatedBy`
) VALUES (
    1,
    1,
    'CustomField2',
    '<>',
    '1234567890',
    TRUE,
    202601,
    NOW(),
    NOW(),
    NULL,
    NULL
);

INSERT INTO `StandaloneSellPriceTemplate` (
    `Id`,
    `Name`,
    `Description`,
    `stratificationFields`,
    `ApplyFieldName`,
    `createdPeriodId`,
    `IsActive`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    1,
    'ChargeBee Standalone Sell Price-1',
    'ChargeBee Standalone Sell Price-1',
    'ProductFamily:ProductLine:ProductClass:ProductCategory',
    NULL,
    202601,
    TRUE,
    NOW(),
    NOW()
);

INSERT INTO `StandaloneSellPriceHierarchy` (
    `seq`,
    `StandaloneSellPriceTemplateId`,
    `StandaloneSellPriceTemplateName`,
    `ArrangementGroupingTemplateId`,
    `IsActive`,
    `createdPeriodId`,
    `CreatedAt`,
    `UpdatedAt`
) VALUES (
    1,
    1,
    'ChargeBee Standalone Sell Price-1',
    1,
    TRUE,
    202601,
    NOW(),
    NOW()
);

INSERT INTO `StandaloneSellPriceBatchHeader` (
    `Id`,
    `StandaloneSellPriceTemplateId`,
    `Name`,
    `Description`,
    `SspType`,
    `EffectiveFromDate`,
    `EffectiveToDate`,
    `Status`,
    `createdPeriodId`,
    `CreatedBy`,
    `CreatedAt`,
    `UpdatedBy`,
    `UpdatedAt`,
    `IsActive`
) VALUES (
    10000,
    1,
    'January 2026 Q1 Standalone Sell Price',
    'January 2026 Q1 Standalone Sell Price',
    'SSP',
    '2026-01-01',
    '2026-03-31',
    'Final',
    202601,
    NULL,
    NOW(),
    NULL,
    NOW(),
    TRUE
);

INSERT INTO `StandaloneSellPriceBatchDetails` (
    `Id`,
    `BatchId`,
    `StandaloneSellPriceTemplateId`,
    `attributeField1`,
    `attributeField2`,
    `attributeField3`,
    `attributeField4`,
    `attributeField5`,
    `attributeField6`,
    `attributeField7`,
    `attributeField8`,
    `attributeField9`,
    `attributeField10`,
    `aboveSspPercentage`,
    `sspPercentage`,
    `belowSspPercentage`,
    `aboveSspPrice`,
    `sspPrice`,
    `belowSspPrice`,
    `IsActive`,
    `createdPeriodId`,
    `CreatedAt`,
    `UpdatedAt`,
    `CreatedBy`,
    `UpdatedBy`
) VALUES (
    10000,
    10000,
    1,
    'Hosted Applications',
    'Revenue Cloud Platform',
    'Enterprise Subscription',
    'SaaS Recurring',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    90,
    80,
    70,
    Null,
    Null,
    Null,
    TRUE,
    202601,
    NOW(),
    NOW(),
    NULL,
    NULL
);


INSERT INTO `JournalAccountsSetup` (
    `id`,
    `name`,
    `description`,
    `SegmentPosition1`,
    `SegmentPosition2`,
    `SegmentPosition3`,
    `SegmentPosition4`,
    `SegmentPosition5`,
    `SegmentPosition6`,
    `SegmentPosition7`,
    `SegmentPosition8`,
    `SegmentPosition9`,
    `SegmentPosition10`,
    `isActive`,
    `createdAt`,
    `updatedAt`
) VALUES (
    1,
    'Revenue Liability',
    'Revenue Liability Account',
    'RevenueSegment1',
    'RevenueSegment2',
    'RevenueSegment3',
    'RevenueSegment4',
    'RevenueSegment5',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    TRUE,
    NOW(),
    NOW()
);


INSERT INTO `JournalAccountsSetup` (
    `id`,
    `name`,
    `description`,
    `SegmentPosition1`,
    `SegmentPosition2`,
    `SegmentPosition3`,
    `SegmentPosition4`,
    `SegmentPosition5`,
    `SegmentPosition6`,
    `SegmentPosition7`,
    `SegmentPosition8`,
    `SegmentPosition9`,
    `SegmentPosition10`,
    `isActive`,
    `createdAt`,
    `updatedAt`
) VALUES (
    2,
    'Contract Liability',
    'Contract Liability Account',
    'DeferSegment1',
    'DeferSegment2',
    'DeferSegment3',
    'DeferSegment4',
    'DeferSegment5',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    TRUE,
    NOW(),
    NOW()
);

INSERT INTO `JournalAccountsSetup` (
    `id`,
    `name`,
    `description`,
    `SegmentPosition1`,
    `SegmentPosition2`,
    `SegmentPosition3`,
    `SegmentPosition4`,
    `SegmentPosition5`,
    `SegmentPosition6`,
    `SegmentPosition7`,
    `SegmentPosition8`,
    `SegmentPosition9`,
    `SegmentPosition10`,
    `isActive`,
    `createdAt`,
    `updatedAt`
) VALUES (
    3,
    'Allocation Liability',
    'Allocation Liability Account',
    'DeferSegment1',
    'DeferSegment2',
    'DeferSegment3',
    'DeferSegment4',
    'CustomSegment1',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    TRUE,
    NOW(),
    NOW()
);


INSERT INTO `JournalAccountsSetup` (
    `id`,
    `name`,
    `description`,
    `SegmentPosition1`,
    `SegmentPosition2`,
    `SegmentPosition3`,
    `SegmentPosition4`,
    `SegmentPosition5`,
    `SegmentPosition6`,
    `SegmentPosition7`,
    `SegmentPosition8`,
    `SegmentPosition9`,
    `SegmentPosition10`,
    `isActive`,
    `createdAt`,
    `updatedAt`
) VALUES (
    4,
    'Allocation Revenue',
    'Allocation Revenue Account',
    'RevenueSegment1',
    'RevenueSegment2',
    'RevenueSegment3',
    'RevenueSegment4',
    '200001',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    TRUE,
    NOW(),
    NOW()
);


insert into arrangementHead(
    ArrangementId BIGINT PRIMARY KEY,
    version BIGINT,
    TotalSellPrice DECIMAL(65, 27),
    TotalListPrice DECIMAL(65, 27),
    TotalCarveAmount DECIMAL(65, 27),
    createdPeriodId BIGINT,
    InitialContractModificationDate DATE,
    ContractModificationDate DATE,
    isArrangementPosted BOOLEAN,
    AllocationTreatment VARCHAR(1),
    CreatedBy VARCHAR(255),
    CreatedAt DATETIME,
    UpdatedBy VARCHAR(255),
    UpdatedAt DATETIME,
    IsActive BOOLEAN
) values ( 
    10000,
    1,
    350,
    400,
    100,
    202601,
    '2026-01-31',
    '2026-01-31',
    false,
    'Retrospective',
    'system',
    NOW(),
    'system',
    NOW(),
 );

 insert into arrangementHead(
    ArrangementId BIGINT PRIMARY KEY,
    version BIGINT,
    TotalSellPrice DECIMAL(65, 27),
    TotalListPrice DECIMAL(65, 27),
    TotalCarveAmount DECIMAL(65, 27),
    createdPeriodId BIGINT,
    InitialContractModificationDate DATE,
    ContractModificationDate DATE,
    isArrangementPosted BOOLEAN,
    AllocationTreatment VARCHAR(1),
    CreatedBy VARCHAR(255),
    CreatedAt DATETIME,
    UpdatedBy VARCHAR(255),
    UpdatedAt DATETIME,
    IsActive BOOLEAN
) values ( 
    10002,
    2,
    1350,
    1400,
    1100,
    202601,
    '2026-01-31',
    '2026-02-28',
    false,
    'Prospective',
    'system',
    NOW(),
    'system',
    NOW(),
 );