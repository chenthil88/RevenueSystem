package com.revrec.engine.common.metadataservice.JournalAccountsSetup;

import com.revrec.engine.common.accountdetails.AccountDetailsRecord;
import java.util.Set;

/**
 * Resolves a segment position spec (field name or literal) from an account-detail row.
 */
final class JournalAccountSegmentResolver {

    private static final Set<String> KNOWN_SEGMENT_FIELDS = Set.of(
            "DeferSegment1", "DeferSegment2", "DeferSegment3", "DeferSegment4", "DeferSegment5",
            "DeferSegment6", "DeferSegment7", "DeferSegment8", "DeferSegment9", "DeferSegment10",
            "RevenueSegment1", "RevenueSegment2", "RevenueSegment3", "RevenueSegment4", "RevenueSegment5",
            "RevenueSegment6", "RevenueSegment7", "RevenueSegment8", "RevenueSegment9", "RevenueSegment10",
            "customSegment1", "customSegment2", "customSegment3", "customSegment4", "customSegment5",
            "customSegment6", "customSegment7", "customSegment8", "customSegment9", "customSegment10");

    private JournalAccountSegmentResolver() {}

    static String resolve(String positionSpec, AccountDetailsRecord accountDetails) {
        if (positionSpec == null || positionSpec.isBlank()) {
            return null;
        }
        String trimmed = positionSpec.trim();
        if (isSegmentField(trimmed)) {
            return resolveFromSource(trimmed, accountDetails);
        }
        return trimmed;
    }

    private static boolean isSegmentField(String spec) {
        if (KNOWN_SEGMENT_FIELDS.contains(spec)) {
            return true;
        }
        return KNOWN_SEGMENT_FIELDS.stream().anyMatch(field -> field.equalsIgnoreCase(spec));
    }

    private static String resolveFromSource(String fieldName, AccountDetailsRecord source) {
        if (source == null) {
            return null;
        }
        return switch (normalizeFieldName(fieldName)) {
            case "defersegment1" -> source.deferSegment1();
            case "defersegment2" -> source.deferSegment2();
            case "defersegment3" -> source.deferSegment3();
            case "defersegment4" -> source.deferSegment4();
            case "defersegment5" -> source.deferSegment5();
            case "defersegment6" -> source.deferSegment6();
            case "defersegment7" -> source.deferSegment7();
            case "defersegment8" -> source.deferSegment8();
            case "defersegment9" -> source.deferSegment9();
            case "defersegment10" -> source.deferSegment10();
            case "revenuesegment1" -> source.revenueSegment1();
            case "revenuesegment2" -> source.revenueSegment2();
            case "revenuesegment3" -> source.revenueSegment3();
            case "revenuesegment4" -> source.revenueSegment4();
            case "revenuesegment5" -> source.revenueSegment5();
            case "revenuesegment6" -> source.revenueSegment6();
            case "revenuesegment7" -> source.revenueSegment7();
            case "revenuesegment8" -> source.revenueSegment8();
            case "revenuesegment9" -> source.revenueSegment9();
            case "revenuesegment10" -> source.revenueSegment10();
            case "customsegment1" -> source.customSegment1();
            case "customsegment2" -> source.customSegment2();
            case "customsegment3" -> source.customSegment3();
            case "customsegment4" -> source.customSegment4();
            case "customsegment5" -> source.customSegment5();
            case "customsegment6" -> source.customSegment6();
            case "customsegment7" -> source.customSegment7();
            case "customsegment8" -> source.customSegment8();
            case "customsegment9" -> source.customSegment9();
            case "customsegment10" -> source.customSegment10();
            default -> null;
        };
    }

    private static String normalizeFieldName(String fieldName) {
        return fieldName.trim().toLowerCase();
    }
}
