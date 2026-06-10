package com.revrec.engine.common.persistence;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Timestamp-based in-memory id generator. Combines millisecond time (relative to a fixed epoch)
 * with a per-millisecond sequence counter (12 bits, up to 4096 ids/ms).
 */
public final class SequenceIdGenerator {

    /** Custom epoch offset (Jan 1, 2026) to keep generated numbers smaller. */
    private static final long EPOCH_OFFSET = 1767225600000L;

    private static final AtomicLong lastTimestamp = new AtomicLong(-1);
    private static final AtomicLong counter = new AtomicLong(0);

    /** Max sequence bits (12 bits allows 0–4095 sequences per millisecond). */
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS);

    private SequenceIdGenerator() {}

    public static long nextId() {
        while (true) {
            long currentTimestamp = System.currentTimeMillis() - EPOCH_OFFSET;
            long lastTime = lastTimestamp.get();

            if (currentTimestamp < lastTime) {
                throw new IllegalStateException("Clock moved backwards. Refusing to generate ID.");
            }

            long sequence;
            if (currentTimestamp == lastTime) {
                sequence = counter.incrementAndGet() & MAX_SEQUENCE;
                if (sequence == 0) {
                    continue;
                }
            } else if (lastTimestamp.compareAndSet(lastTime, currentTimestamp)) {
                counter.set(0);
                sequence = 0;
            } else {
                continue;
            }

            return (currentTimestamp << SEQUENCE_BITS) | sequence;
        }
    }
}
