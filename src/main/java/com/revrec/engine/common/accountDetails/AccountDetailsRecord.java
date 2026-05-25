package com.revrec.engine.common.accountdetails;

/**
 * Shared segment columns for order and billing account-detail rows. Implemented by
 * {@link com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails.RevenueContractOrderAccountDetailsRecord}
 * and
 * {@link com.revrec.engine.domain.service.RevenueContractBillingAccountDetails.RevenueContractBillingAccountDetailsRecord}.
 */
public interface AccountDetailsRecord {

    String deferSegment1();

    String deferSegment2();

    String deferSegment3();

    String deferSegment4();

    String deferSegment5();

    String deferSegment6();

    String deferSegment7();

    String deferSegment8();

    String deferSegment9();

    String deferSegment10();

    String revenueSegment1();

    String revenueSegment2();

    String revenueSegment3();

    String revenueSegment4();

    String revenueSegment5();

    String revenueSegment6();

    String revenueSegment7();

    String revenueSegment8();

    String revenueSegment9();

    String revenueSegment10();

    String customSegment1();

    String customSegment2();

    String customSegment3();

    String customSegment4();

    String customSegment5();

    String customSegment6();

    String customSegment7();

    String customSegment8();

    String customSegment9();

    String customSegment10();
}
