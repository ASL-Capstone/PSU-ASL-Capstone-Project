package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Statistics;
import com.psu.capstonew17.backend.api.Test;

/**
 * Created by Doug Whitley on 5/1/2017.
 */

public class testMultiChoiceTest implements Test {
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Question next() {
        return new testMultiChoiceQuestion();
    }

    @Override
    public Statistics getStats() {
        return null;
    }
}
