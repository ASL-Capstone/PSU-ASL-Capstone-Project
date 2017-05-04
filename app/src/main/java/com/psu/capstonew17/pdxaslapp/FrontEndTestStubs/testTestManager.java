package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Test;
import com.psu.capstonew17.backend.api.TestManager;

import java.util.List;

/**
 * Created by Doug Whitley on 5/1/2017.
 */

public class testTestManager implements TestManager {
    @Override
    public Test buildTest(List<Deck> sources, Options opts) {
        switch (opts.questionTypes) {
            case 0x01:
                return new testMultiChoiceTest();
            default:
                return null;
        }
    }
}
