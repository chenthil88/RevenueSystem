package com.revrec.engine.common.metadataservice.JournalAccountsSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Journal account value built from {@link JournalAccountsSetup} segment positions and an account-detail row.
 * Use {@link #delimitedAccountValue()} to persist or split; use {@link #segment(int)} for a single position.
 */
public record DerivedJournalAccountValue(
        String journalAccountName,
        String segment1,
        String segment2,
        String segment3,
        String segment4,
        String segment5,
        String segment6,
        String segment7,
        String segment8,
        String segment9,
        String segment10) {

    /** Delimiter between segment values in {@link #delimitedAccountValue()}. */
    public static final String DELIMITER = "|";

    /**
     * Full account string with {@link #DELIMITER} between non-blank segments (e.g. {@code R1|R2|200001}).
     */
    public String delimitedAccountValue() {
        List<String> parts = nonBlankSegments();
        if (parts.isEmpty()) {
            return "";
        }
        return String.join(DELIMITER, parts);
    }

    /** Same as {@link #delimitedAccountValue()}; kept for backward compatibility. */
    public String accountValue() {
        return delimitedAccountValue();
    }

    /**
     * Splits {@link #delimitedAccountValue()} back into segment tokens (empty list if none).
     */
    public List<String> splitDelimitedAccountValue() {
        String value = delimitedAccountValue();
        if (value.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(value.split("\\" + DELIMITER, -1));
    }

    /** Non-blank segments in position order (segment1 .. segment10). */
    public List<String> nonBlankSegments() {
        List<String> parts = new ArrayList<>(10);
        for (String segment : segments()) {
            if (segment != null && !segment.isBlank()) {
                parts.add(segment);
            }
        }
        return List.copyOf(parts);
    }

    /** 1-based segment position (1–10); returns null if out of range. */
    public String segment(int position) {
        return switch (position) {
            case 1 -> segment1;
            case 2 -> segment2;
            case 3 -> segment3;
            case 4 -> segment4;
            case 5 -> segment5;
            case 6 -> segment6;
            case 7 -> segment7;
            case 8 -> segment8;
            case 9 -> segment9;
            case 10 -> segment10;
            default -> null;
        };
    }

    public List<String> segments() {
        return List.of(segment1, segment2, segment3, segment4, segment5, segment6, segment7, segment8, segment9, segment10);
    }
}
