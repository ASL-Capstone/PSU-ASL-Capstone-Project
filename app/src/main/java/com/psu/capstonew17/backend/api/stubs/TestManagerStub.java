package com.psu.capstonew17.backend.api.stubs;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Test;
import com.psu.capstonew17.backend.api.TestManager;

import java.util.List;
/**
 * Created by Tim on 4/27/2017.
 */

public class TestManagerStub implements TestManager {
    @Override
    public Test buildTest(List<Deck> sources, Options opts) {
        return null;
    }
}
