package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.TestManager;
import com.psu.capstonew17.backend.api.stubs.DeckManagerStub;
import com.psu.capstonew17.backend.api.stubs.DeckStub;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalTestManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Created by Tim on 5/13/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TestInstrumentalTest {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void buildTest() {
        TestManager testManager = ExternalTestManager.getInstance(context);
        DeckManager deckManager = ExternalDeckManager.getInstance(context);
        List<Deck> deckList = deckManager.getDecks("shouldBeEmpty");
        deckList.add(new DeckStub());
        deckList.add(new DeckStub());
        com.psu.capstonew17.backend.api.Test test = testManager.buildTest(deckList, null);

        assertNotEquals(null, test);
        assertEquals(null, test.getStats());
    }
}
