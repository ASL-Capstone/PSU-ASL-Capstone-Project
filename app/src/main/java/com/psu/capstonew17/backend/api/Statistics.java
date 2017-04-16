package com.psu.capstonew17.backend.api;



//TODO Resolve time.Duration imports
//import java.time.Duration;
import java.util.List;

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
    //Duration getAverageAnswerTime();
}
