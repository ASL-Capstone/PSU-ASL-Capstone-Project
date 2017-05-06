//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;
import java.util.List;
import javax.xml.datatype.Duration;

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
    Long getAverageAnswerTime();
}
