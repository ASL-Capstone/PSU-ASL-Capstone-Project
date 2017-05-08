//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

import java.util.List;
import android.util.Pair;

/**
 * Public interface for questions, as generated during a test. Note that this is *not* the same
 * thing as a Card - Question objects contain a type and are immutable, whereas Card objects directly
 * represent the cards stored in the database.
 */
public interface Question {
    enum Type {
        MULTIPLE_CHOICE,
        TEXT_ENTRY
    }

    /**
     * Get the video object associated with this question.
     */
    Video getVideo();

    /**
     * Get the type of this question
     */
    Type getType();

    /**
     * Return the available options if this is a multiple-choice question, or null othewise.
     */
    List<String> getOptions();

    /**
     * Check and record an answer for this question
     *
     * @param answer The answer given, as a case-insensitive string
     * @return Whether the given answer is correct
     */
    Pair<Boolean, String> answer(String answer);
}
