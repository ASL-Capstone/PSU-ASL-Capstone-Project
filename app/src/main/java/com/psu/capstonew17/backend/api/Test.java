//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

import java.util.Iterator;

/**
 * Public test iterator interface.
 */
public interface Test extends Iterator<Question> {
    /**
     * Get performance statistics for the user's answers so far.
     * 
     * The resulting object is dynamic, and updates every time the user answers
     * a question.
     */
    Statistics getStats();
}
