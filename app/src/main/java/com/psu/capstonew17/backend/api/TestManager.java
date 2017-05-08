//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;
import java.util.List;

public interface TestManager {
    /**
     * Which mode to use when generating the sequence of questions
     */
    enum OrderingMode {
        RANDOM,
        NAIVE_STORAGE
    }

    /**
     * Options for generating a new test
     */
    class Options {
        public static int QUESTION_MULTIPLE_CHOICE = 0x01;
        public static int QUESTION_TEXT_ENTRY = 0x02;

        /**
         * How to generate the sequence of questions for this test
         */
        public OrderingMode mode;

        /**
         * What kind of questions to generate during the test
         */
        public int questionTypes;

        /**
         * Number of questions to generate in the resulting test
         */
        public int count;

        /**
         * Whether to record statistics for this test
         */
        public boolean recordStats;
    }

    /**
     * Construct a new test with the given set of source decks and options.
     *
     * @param sources The decks to take questions from
     * @param opts Test configuration, or null for default
     * @return The resulting test iterator
     */
    Test buildTest(List<Deck> sources, Options opts);
}
