package com.psu.capstonew17.backend.sharing;

import java.util.Random;

/**
 * Sharing protocol constants and utility methods
 */
class Protocol {
    public static final byte MT_CHALLENGE = 0;
    public static final byte MT_RESPONSE = 1;
    public static final byte MT_DECK = 2;
    public static final byte MT_CKSUM = 3;
    public static final byte MT_CKSUM_VALID = 4;
    public static final byte MT_CKSUM_ERROR = 5;
    public static final byte MT_ERROR = 100;

    /**
     * Generate a random sequence of bytes of a given length
     */
    public static byte[] randomBytes(Random r, int length) {
        byte arr[] = new byte[length];
        r.nextBytes(arr);
        return arr;
    }
}
