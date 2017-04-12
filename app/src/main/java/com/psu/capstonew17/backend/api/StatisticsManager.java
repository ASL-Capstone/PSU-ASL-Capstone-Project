package com.com.example.backendtesting.backend.api;

import java.time.Instant;
import java.time.Duration;

/**
 * Statistics manager used to retrieve views over the stored history
 */
public interface StatisticsManager {
    /**
     * Get a view over all results in a given time span.
     */
    Statistics forTimeSpan(Instant start, Duration length);

    /**
     * Get a view over all answers for a given card
     */
    Statistics forCard(Card c);

    /**
     * Get a view over all answers for a given deck
     */
    Statistics forDeck(Deck d);
}
