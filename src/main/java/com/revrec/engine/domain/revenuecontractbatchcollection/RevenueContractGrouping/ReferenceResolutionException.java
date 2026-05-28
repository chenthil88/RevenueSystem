package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

/**
 * Thrown when revenue contract reference resolution fails
 */
public class ReferenceResolutionException extends RuntimeException {
    public ReferenceResolutionException(String message) {
        super(message);
    }

    public ReferenceResolutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
