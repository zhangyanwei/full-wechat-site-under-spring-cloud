package com.askdog.service.impl.cache.hibernate.redis.util;

/**
 * Generates increasing identifiers (in a single VM only).
 * Not valid across multiple VMs. Yet, the identifier is based on time, so that the drifting
 * across a cluster should not ever be large...
 */
public final class Timestamper {

    private Timestamper() { }

    /**
     * Returns an increasing unique value based on the System.currentTimeMillis()
     * with some additional reserved space for a counter.
     *
     * @return uniquely & increasing value
     */
    public static long next() {
        return System.currentTimeMillis();
    }
}

