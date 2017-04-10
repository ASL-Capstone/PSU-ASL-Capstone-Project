package com.example.backendtesting.backend.api;

import java.time.Duration;

/**
 * Represents performance statistics computed over some subset of historical
 * tests.
 */
public interface Statistics {
    /**
     * Get the cards which were answered correctly.
     */
    List<Card> getCorrectCards();

    /**
     * Get the cards which were answered incorrectly.
     */
    List<Card> getIncorrectCards();

    /**
     * Get the number of correct answers
     */
    int getCorrect();

    /**
     * Get the number of incorrect answers
     */
    int getIncorrect();

    /**
     * Get the average time to answer each question
     */
    Duration getAverageAnswerTime();
}
