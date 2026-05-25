## Table and dependency overview

```text
Calendar
CurrentOpenPeriod
Currency
JournalAccountsSetup

PerformanceObligationTemplate --> PerformanceObligationRule --> PerformanceObligationRuleFilter
ArrangementGroupingTemplate --> ArrangementGroupingHierarchy --> ArrangementGroupingFilter --> StandaloneSellPriceHierarchy
StandaloneSellPriceTemplate --> StandaloneSellPriceBatchHeader --> StandaloneSellPriceBatchDetails

contractModificationHeader --> contractModificationDetails

RevRecStage --> RevRecStageHistory

arrangementBatchHeader --> arrangementBatchDetails
arrangementHead --> arrangementPobDetails
arrangementOrderDetails --> arrangementAllocationDetails --> arrangementOrderAccountDetails --> arrangementOrderAttributes
arrangementBillingDetails --> arrangementBillingAccountDetails 
ArrangementContractModificationDetails

RevenueJournalEntries
AllocationJournalEntries
CaClJournalEntries (ContractAccate/ContractLiability)


```


Use Test;

**System Configuration**
```sql
Select * from Calendar;
Select * from CurrentOpenPeriod;
Select * from Currency;
Select * from JournalAccountsSetup;
```

**PerformanceObligationTemplate --> PerformanceObligationRule --> PerformanceObligationRuleFilter**

```sql
Select * from PerformanceObligationTemplate;
Select * from PerformanceObligationRule;
Select * from PerformanceObligationRuleFilter;
```

**ArrangementGroupingTemplate --> ArrangementGroupingHierarchy -->ArrangementGroupingFilter --> StandaloneSellPriceHierarchy**

```sql
Select * from ArrangementGroupingTemplate;
Select * from ArrangementGroupingHierarchy;
Select * from ArrangementGroupingFilter;
Select * from StandaloneSellPriceHierarchy;
```

**StandaloneSellPriceTemplate --> StandaloneSellPriceBatchHeader --> StandaloneSellPriceBatchDetails**

```sql
Select * from StandaloneSellPriceTemplate;
Select * from StandaloneSellPriceBatchHeader;
Select * from StandaloneSellPriceBatchDetails;
```

**contractModificationHeader --> contractModificationDetails**

```sql
Select * from contractModificationHeader;
Select * from contractModificationDetails;
```

**RevRecStage --> RevRecStageHistory**

```sql
Select * from RevRecStage;
Select * from RevRecStageHistory;
```

 
/*   
**arrangementBatchHeader --> arrangementBatchDetails**
         ⬇ ️
**arrangementHead**
         ⬇
**arrangementOrderDetails --> arrangementAllocationDetails --> arrangementOrderAccountDetails --> arrangementOrderAttributes**
         ⬇
**arrangementBillingDetails --> arrangementBillingAccountDetails**
         ⬇
**arrangementPobDetails**
         ⬇
**ArrangementContractModificationDetails**
*/
 

```sql
Select * from arrangementBatchHeader;
Select * from arrangementBatchDetails;
Select * from arrangementHead;

Select * from arrangementOrderDetails;
Select * from arrangementAllocationDetails;
Select * from arrangementOrderAccountDetails;
Select * from  arrangementOrderAttributes;

Select * from arrangementBillingDetails;
Select * from arrangementBillingAccountDetails;

Select * from arrangementPobDetails;

Select * from ArrangementContractModificationDetails;
```

 
**Journal Entries**
 

```sql
Select * from RevenueJournalEntries;
Select * from AllocationJournalEntries;
```

**CaClJournalEntries (ContractAccate/ContractLiability)**

**LongTerm and ShortTerm**

