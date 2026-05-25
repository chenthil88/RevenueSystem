# Accounting Plan Business Logic

This document extracts the business logic from `packages/accounting/src/core/plan`
so the same revenue or expense planning behavior can be rebuilt in another
technology stack with a different table structure.

The key implementation point is to keep the plan engine schema-independent.
Load data from your new tables into a normalized input DTO, run the plan
algorithm, then map the returned rows into your new persistence model.

## Normalized Contract

### Plan input DTO

Use a DTO like this in the new project:

```text
PlanInput
  recognitionRuleId        POINT_IN_TIME, RATABLE, etc.
  planType                 default, daily, fixed, monthly_endmonth_exclusive,
                           true30/360, custom
  startDate                service or delivery start date, YYYY-MM-DD
  endDate                  extended delivery end date if present, else delivery
                           end date, YYYY-MM-DD
  amount                   product revenue or allocated expense to recognize
  endDateInclusive         org setting, default false
  exactStartDays           org setting for fixed plan, default false
  term                     contract term in months, optional
  recognizedOn             startDate or endDate for point-in-time, default startDate
  calendarConfig           default fiscal calendar or retail calendar config
  arrangementEffectiveDate used only if zero rows must be prepended before start
  maxContractYears         default 20
```

Current RevLock field mapping:

```text
startDate                revenueArrangementItem.deliveryStartDate
endDate                  revenueArrangementItem.extendedDeliveryEndDate
                         || revenueArrangementItem.deliveryEndDate
amount                   productRevenue or expenseItem.allocatedExpense
term                     revenueArrangementItem.salesOrderItem.term
recognizedOn             revenueArrangementItem.ssp.recognizedOn
calendarConfig           org config properties/calendar value config
planType                 org config properties/ratable_plan_type
endDateInclusive         org config properties/ratable_end_date_inclusive
exactStartDays           org config properties/ratable_start_exact_days
maxContractYears         org config properties/max-contract-years
```

### Plan output DTO

The core engine should return only the minimum business output:

```text
PlanRow
  period                  accounting period label, usually YYYYMM
  periodStartDate         start date of the accounting period
  periodEndDate           end/effective date of the accounting period
  amount                  amount recognized in this period
  percentRecognized       optional, only needed for later reallocation logic
```

The current code returns `{ actgPeriod, planAmount }`, where `actgPeriod`
contains the period label and calendar metadata. New table columns should be
populated by mapping this DTO, not by embedding table logic in the algorithms.

## Required Date Adapter

The plan logic depends more on calendar behavior than on database structure.
Recreate or wrap these operations in the new technology:

```text
isBefore(dateA, dateB)
daysBetween(dateA, dateB, includeStart, includeEnd)
toActgPeriod(dateOrPeriod, calendarConfig)
periodsBetween(startPeriod, endPeriod, includeStart, includeEnd, calendarConfig)
priorPeriod(period, calendarConfig)
toStartOfMonth(date)
toEndOfMonth(date)
getDayOfMonth(date)
getDaysInMonth(month, year)
addDays(date, count)
addMonths(date, count)
toComponents(date) -> year, month, day
calculateEndDateBasedOnTrue30DayTerm(startDate, term)
```

`toActgPeriod` must return a rich object, not just a string:

```text
ActgPeriod
  period                  accounting period label
  startDate               first date in period
  endDate                 last date in period
  strMonth                month as two-character string
  numberOfDays()          period length, retail calendars use weeks * 7
  get(periodOrDate)       returns another ActgPeriod in the same calendar
```

Retail and shifted fiscal calendars are handled by this adapter. If the adapter
is wrong, the plan math will be wrong even if the formulas are copied exactly.

## Plan Selection

The dispatcher behavior in `ArrangementUtils` is:

```text
if recognitionRuleId == POINT_IN_TIME:
  run PointInTimePlan
else:
  planType = org config properties/ratable_plan_type || "default"

  if calendar type is retail and retail weekGrouping is not Group444:
    only planType "daily" is allowed

  if planType == "monthly_endmonth_exclusive":
    run FullMonthRatablePlan
  else if planType == "fixed":
    if exactStartDays:
      run Modified30_360Plan
    else:
      run FixedMonthRatablePlan
  else if planType == "daily":
    run DailyRatablePlan
  else if planType == "true30/360":
    run True30_360Plan
  else:
    run ApportionedRatablePlan

truncate returned rows to maxContractYears * 12
validate every row has a period and numeric amount
```

For `true30/360`, if these org settings are true:

```text
properties/true30_360/start_date_inclusive = true
properties/dereference_rule_has_priority = true
term is present
```

then calculate a new end date from `startDate` and `term` using true 30-day
months before running `True30_360Plan`.

## Shared Rules

All plans reject `endDate < startDate`, except `ApportionedRatablePlan`, which
returns an empty plan after warning.

Most ratable plans split an amount over inclusive accounting periods:

```text
startActgPeriod = toActgPeriod(startDate)
endActgPeriod = toActgPeriod(endDate)
periods = [startPeriod..endPeriod] inclusive
```

When `startDate == endDate`, total revenue days are forced to `1` in the daily
style plans so the full amount can still be recognized.

## Point In Time

Purpose: recognize the full amount in one period, optionally on the end date.

Inputs:

```text
startDate, endDate, amount, recognizedOn, calendarConfig
```

Algorithm:

```text
require startDate
startPeriod = toActgPeriod(startDate)
plan = [{ period: startPeriod, amount }]

if endDate is missing:
  return plan

endPeriod = toActgPeriod(endDate)
for every period after startPeriod through endPeriod:
  append zero-amount row

if recognizedOn == "endDate":
  set first row amount = 0
  set last row amount = amount
```

Notes:

- The zero rows preserve the timeline through the end date.
- `recognizedOn` currently comes from SSP configuration.

## Daily Ratable

Purpose: recognize by actual service days in each accounting period.

Inputs:

```text
startDate, endDate, amount, endDateInclusive, calendarConfig
```

Algorithm:

```text
if endDate < startDate: error

startPeriod = toActgPeriod(startDate)
endPeriod = toActgPeriod(endDate)

if startPeriod == endPeriod:
  firstDays = daysBetween(startDate, endDate, true, endDateInclusive)
else:
  firstDays = daysBetween(startDate, startPeriod.endDate, true, true)

lastDays =
  0 if startPeriod == endPeriod
  else daysBetween(endPeriod.startDate, endDate, true, endDateInclusive)

periods =
  [startPeriod] if startPeriod == endPeriod
  else periodsBetween(startPeriod, endPeriod, true, true)

totalDays =
  1 if startDate == endDate
  else daysBetween(startDate, endDate, true, endDateInclusive)

if firstDays == 0 and lastDays == 0 and totalDays == 1:
  firstDays = 1

dailyRate = amount / totalDays

for each period:
  if period is first: amount = firstDays * dailyRate
  else if period is last: amount = lastDays * dailyRate
  else: amount = period.numberOfDays() * dailyRate
```

## Fixed Month Ratable

Purpose: recognize using 30-day months. First and last periods are prorated by
30-day month math.

Inputs:

```text
startDate, endDate, amount, endDateInclusive, exactDays, calendarConfig
```

Algorithm:

```text
if calendar type is retail: error
if endDate < startDate: error

startPeriod = toActgPeriod(startDate)
endPeriod = toActgPeriod(endDate)

firstDays =
  30 if startDate == endDate
  else (exactDays ? startPeriod.numberOfDays() : 30) - day(startDate) + 1

lastDays =
  0 if startDate == endDate
  else day(endDate) - (endDateInclusive ? 0 : 1)

if endDateInclusive and lastDays != 0 and
   ((end month is not February and lastDays > 30) or
    (end month is February and endDate is period endDate)):
  lastDays = 30

periods = periodsBetween(startPeriod, endPeriod, true, true)

if startDate != endDate and startPeriod == endPeriod:
  firstDays = lastDays
  lastDays = 0

totalDays = firstDays + lastDays + 30 * count(middle periods)

if totalDays == 0:
  totalDays = 1
  firstDays = 1

rate = amount / totalDays

for each period:
  if first: amount = firstDays * rate
  else if last: amount = lastDays * rate
  else: amount = 30 * rate
```

## Modified 30/360

Purpose: recognize by terms where a full month equals one term, with first
period prorated by actual days and the last period absorbing the residual.

Inputs:

```text
startDate, endDate, amount, endDateInclusive, calendarConfig, terms
```

Algorithm:

```text
if endDate < startDate: error

startPeriod = toActgPeriod(startDate)
endPeriod = toActgPeriod(endDate)

firstDays and lastDays are calculated with actual daysBetween, same as Daily
totalContractRevenueDays = daysBetween(startDate, endDate, true, endDateInclusive)
if startDate == endDate: totalContractRevenueDays = 1

periods = periodsBetween(startPeriod, endPeriod, true, true)

if terms is present:
  totalTerms = terms
else:
  totalDays = 0
  for each period:
    if first period:
      if firstDays > 30:
        totalDays += 30
      else:
        actualMonthDays = startPeriod.numberOfDays() <= 30
          ? 30
          : startPeriod.numberOfDays() - 1
        totalDays += actualMonthDays - (day(startDate) - 1)
    else if last period:
      if lastDays > 30:
        totalDays += 30
      else if startDate is start of month and endPeriod is February
              and endDate is February period endDate:
        totalDays += 30
      else:
        totalDays += lastDays
    else:
      totalDays += 30

  totalTerms = totalDays / 30

if only one period and startPeriod == endPeriod:
  totalContractRevenueDays = 1
  firstDays = startPeriod.numberOfDays()
  totalTerms = 1

amountPerTerm = amount / totalTerms

if firstDays == 0 and lastDays == 0 and totalContractRevenueDays == 1:
  firstDays = 1

for each period:
  if first:
    dailyAmountInStartMonth = amountPerTerm / startPeriod.numberOfDays()
    amount = firstDays * dailyAmountInStartMonth
  else if last:
    amount = original amount - cumulativeAmount
  else:
    amount = amountPerTerm
```

Important behavior:

- The last period is always the balancing row.
- `terms` overrides derived term count.
- February month-end has explicit full-month treatment in term derivation.

## Full Month Ratable

Purpose: recognize equally by full accounting months. A partial ending month is
excluded unless the end date is month end.

Inputs:

```text
startDate, endDate, amount, calendarConfig
```

Algorithm:

```text
if endDate < startDate: error

startPeriod = toActgPeriod(startDate)
endPeriod = toActgPeriod(endDate)
endDateEndOfMonth = toEndOfMonth(endDate)

if endPeriod == startPeriod:
  finalPeriod = startPeriod
else if endDate < endDateEndOfMonth:
  finalPeriod = priorPeriod(endPeriod)
else:
  finalPeriod = endPeriod

periods = periodsBetween(startPeriod, finalPeriod, true, true)
monthCount = max(1, periods.length)
monthlyAmount = amount / monthCount

for each period:
  amount = monthlyAmount
```

Important behavior:

- The starting period is counted even when the start date is mid-month.
- The ending period is dropped when the end date is before month-end.

## Apportioned Ratable

Purpose: split by percentages. First and last periods use day-based percentages
when they are shorter than 28 days; periods with 28 or more days are treated as
full monthly periods.

Inputs:

```text
startDate, endDate, amount, endDateInclusive, calendarConfig
```

Algorithm:

```text
if endDate < startDate:
  warn and return []

startPeriod = toActgPeriod(startDate)
endPeriod = toActgPeriod(endDate)

firstDays and lastDays are calculated with actual daysBetween, same as Daily
periods = periodsBetween(startPeriod, endPeriod, true, true)

totalDays =
  1 if startDate == endDate
  else daysBetween(startDate, endDate, true, endDateInclusive)

if firstDays == 0 and lastDays == 0 and totalDays == 1:
  firstDays = 1

totalPeriods = 1 if startDate == endDate else periods.length

firstPercent = firstDays / totalDays if firstDays < 28 else null
lastPercent = lastDays / totalDays if lastDays < 28 else null

remainingPercent = 1 - valueOrZero(firstPercent) - valueOrZero(lastPercent)
remainingDuration = totalPeriods
if lastPercent is not null and totalPeriods > 1: remainingDuration -= 1
if firstPercent is not null: remainingDuration -= 1

monthlyPercent = remainingPercent / remainingDuration

if firstDays >= 28: firstPercent = monthlyPercent
if lastDays >= 28: lastPercent = monthlyPercent

for each period:
  if first: amount = firstPercent * original amount
  else if last: amount = lastPercent * original amount
  else: amount = monthlyPercent * original amount
```

Important behavior:

- JavaScript treats `null` as zero in the current `remainingPercent` math.
  Reimplement this intentionally as `valueOrZero`.
- The build method does not persist `percentRecognized`, but the same
  percentage math is useful if your new table stores it.

### updateRatablePlan

This helper recalculates existing plan rows from stored `percentRecognized`.

```text
if original amount == 0:
  set every row amount = 0
  set every row percentRecognized = 0
  return

remainingAmount = original amount

for each row:
  require numeric percentRecognized
  row.amount = row.percentRecognized * original amount

  if row amount exceeds remaining amount, using absolute comparison for
     negative amounts:
    row.amount = remainingAmount
    row.percentRecognized = row.amount / original amount

  remainingAmount -= row.amount

  if last row and remainingAmount is not approximately zero:
    row.amount += remainingAmount
    row.percentRecognized = row.amount / original amount
    remainingAmount = 0

  require numeric row.amount
```

## True 30/360

Purpose: recognize using a strict 30-day month and 360-day year model with many
end-of-month and February adjustments.

Inputs:

```text
startDate, endDate, amount, startDateInclusive, calendarConfig,
salesOrder, endDateAdjustedForTerm
```

Defaults:

```text
startDateInclusive defaults to true
endDateAdjustedForTerm defaults to false
```

Core algorithm:

```text
if endDate < startDate: error

break startDate and endDate into year, month, day
startDayMax30 = min(30, startDay)
endDayMax30 = min(30, endDay + (startDateInclusive ? 1 : 0))

if startDate == endDate and startDateInclusive:
  keep dates as-is and treat as one-day revenue later
else if startDay == endDay and startDay is not start-month end
        and startDateInclusive == false:
  move startDate forward by 1 day and recompute start components
else if startDay == endDay and startDateInclusive == true
        and endDateAdjustedForTerm == false:
  move endDate backward by 1 day and recompute end components
  movedEndDay = true

numYears = endYear - startYear
numMonths = endMonth - startMonth
adjustedDays = endDayMax30 - startDayMax30
```

Special day adjustments:

```text
if end is Feb 27 and startDay > 27:
  adjustedDays = 0
else if start is Feb 28 and endDay >= 28:
  adjustedDays = 0
else if end is Feb 28 in leap year:
  adjustedDays = 0
else if start is Feb 29 in leap year and endDay >= 30:
  adjustedDays = 0
else if start and end are both in Feb and endDay is start-month end:
  adjustedDays = 30
else if startDay == 1 and end is Feb 28 or later:
  adjustedDays = 30
else if startDateInclusive == false and startDay == endDay + 1:
  adjustedDays = 0
else if same year, different month, and endDay is end-month end:
  adjustedDays += 1

adjustedDays = min(30, adjustedDays)
revenueDays = 360 * numYears + 30 * numMonths + adjustedDays

if endDateAdjustedForTerm:
  rounded = round(revenueDays / 30) * 30
  if rounded > 0: revenueDays = rounded
```

First-period day allocation:

```text
if startDayMax30 >= 30 or start is the last day of February:
  daysBeforeNextPeriod = 1
else:
  daysBeforeNextPeriod = 30 - startDayMax30 + 1

if start month is February and endDay <= 28 and start is February month end:
  if startDay == endDay + 1 and movedEndDay:
    daysBeforeNextPeriod = 2
  else:
    daysBeforeNextPeriod = min(30 - startDay + 1, 30 - endDay)
```

Amount allocation:

```text
periods = periodsBetween(startPeriod, endPeriod, true, true)
realPeriodCount = max(1, round(revenueDays / 30))
amountPerDay = original amount / revenueDays
runningAmount = 0

for each period:
  if first period:
    periodAmount = daysBeforeNextPeriod * amountPerDay
    if realPeriodCount == 1 and startMonth == endMonth:
      periodAmount = original amount
  else if last period:
    periodAmount = original amount - runningAmount
  else:
    periodAmount = 30 * amountPerDay

  runningAmount += periodAmount
```

Important behavior:

- The final period absorbs any residual amount.
- The algorithm intentionally moves either start or end dates for same-day-of-
  month contracts depending on start-date inclusivity.
- February and end-of-month behavior is not standard library date math. Port
  these cases exactly and cover them with tests.

## Post Processing: Zero Rows Before First Plan Period

The arrangement layer may prepend zero-amount rows before the first plan period.
This is not part of the individual plan classes, but it affects persisted plan
rows.

```text
if plan is empty: do nothing

arrangementPeriod = toActgPeriod(arrangementEffectiveDate)
firstPlanPeriod = first row period

if arrangementPeriod >= firstPlanPeriod: do nothing

for each period from arrangementPeriod through the period before firstPlanPeriod:
  prepend row with amount = 0
```

## New Table Structure Guidance

Because your new table structure is different, do not try to mirror the old
object graph. Build adapters at the boundaries:

1. Read your source tables and produce `PlanInput`.
2. Run the selected pure plan algorithm.
3. Convert each `PlanRow` into your target plan table columns.
4. Store any audit fields, IDs, posting dates, or cumulative totals outside the
   core algorithm.

Suggested target separation:

```text
plan_engine/
  date_adapter
  plan_dispatcher
  point_in_time
  daily_ratable
  fixed_month_ratable
  modified_30_360
  full_month_ratable
  apportioned_ratable
  true_30_360

application/
  load_plan_input_from_new_schema
  save_plan_rows_to_new_schema
  plan_post_processing
```

## Acceptance Test Sources

Use the current Jest tests and JSON snapshots as source-of-truth examples:

```text
Daily                 packages/accounting/src/__tests__/DailyRatablePlanTest.js
Fixed month           packages/accounting/src/__tests__/FixedMonthRatablePlanTest.js
Modified 30/360       packages/accounting/src/__tests__/Modified30_360PlanTest.js
Full month            packages/accounting/src/__tests__/FullMonthRatablePlanTest.js
Apportioned           packages/accounting/src/__tests__/ApportionedRatablePlanTest.js
True 30/360           packages/accounting/src/__tests__/True30_360PlanTest.js
Snapshots             packages/accounting/src/__tests__/output/
```

Minimum regression coverage for the new project:

```text
same start and end date
single period with different start and end date
multi-period contract with partial first and last periods
endDateInclusive true and false
February 28 and February 29
month-end day 30 and 31
negative amount
zero amount
shifted fiscal calendar with lastMonthOfYear
retail calendar daily plan
true30/360 with dereference_rule_has_priority and term
point-in-time recognizedOn startDate and endDate
```

## Source Files

```text
packages/accounting/src/core/ArrangementUtils.js
packages/accounting/src/core/plan/PointInTimePlan.js
packages/accounting/src/core/plan/DailyRatablePlan.js
packages/accounting/src/core/plan/FixedMonthRatablePlan.js
packages/accounting/src/core/plan/Modified30_360Plan.js
packages/accounting/src/core/plan/FullMonthRatablePlan.js
packages/accounting/src/core/plan/ApportionedRatablePlan.js
packages/accounting/src/core/plan/True30_360Plan.js
packages/webutils/src/Period.js
```
