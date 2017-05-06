package com.psu.capstonew17.backend.api.stubs;

import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Statistics;
import com.psu.capstonew17.backend.api.Test;

import java.util.Iterator;

/**
 * Created by Tim on 4/27/2017.
 */

public class TestStub implements Test {
    @Override
    public Statistics getStats() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Question next() {
        return null;
    }
}
