package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition;

/**
 * Plan type and recognition rule identifiers from org config / SSP setup.
 */
public final class RevenueRecognitionPlanConstants {

    public static final String RECOGNITION_RULE_POINT_IN_TIME = "POINT_IN_TIME";
    public static final String RECOGNITION_RULE_RATABLE = "RATABLE";

    public static final String PLAN_TYPE_DEFAULT = "default";
    public static final String PLAN_TYPE_DAILY = "daily";
    public static final String PLAN_TYPE_FIXED = "fixed";
    public static final String PLAN_TYPE_MONTHLY_ENDMONTH_EXCLUSIVE = "monthly_endmonth_exclusive";
    public static final String PLAN_TYPE_TRUE_30_360 = "true30/360";
    public static final String PLAN_TYPE_CUSTOM = "custom";

    public static final String RECOGNIZED_ON_START_DATE = "startDate";
    public static final String RECOGNIZED_ON_END_DATE = "endDate";

    public static final String CALENDAR_TYPE_RETAIL = "retail";
    public static final String RETAIL_WEEK_GROUPING_GROUP_444 = "Group444";

    public static final int DEFAULT_MAX_CONTRACT_YEARS = 20;

    private RevenueRecognitionPlanConstants() {}
}
