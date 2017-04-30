//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

/**
 * Statistics manager used to retrieve views over the stored history
 */
public interface StatisticsManager {
    /**
     * Get a view over all results in a given time span.
     * @param start The start time in milliseconds since the epoch
     * @param length The length in milliseconds
     */
    Statistics forTimeSpan(long start, long length);

    /**
     * Get a view over all answers for a given card
     */
    Statistics forCard(Card c);

    /**
     * Get a view over all answers for a given deck
     */
    Statistics forDeck(Deck d);
}
