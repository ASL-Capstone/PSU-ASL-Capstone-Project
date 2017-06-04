package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.CardManager;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Statistics;
import com.psu.capstonew17.backend.api.TestManager;
import com.psu.capstonew17.backend.api.Test;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.stubs.DeckManagerStub;
import com.psu.capstonew17.backend.api.stubs.DeckStub;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalStatisticsManager;
import com.psu.capstonew17.backend.data.ExternalTestManager;
import com.psu.capstonew17.backend.db.AslDbHelper;

import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tim on 5/13/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TestInstrumentalTest {
    private Context context;
    private AslDbHelper dbHelper;
    private CardManager cardManager;
    private DeckManager deckManager;
    private TestManager testManager;
    private Deck deck;
    private com.psu.capstonew17.backend.api.Test test;
    private TestManager.Options options;


    @Before
    public void setup() throws Exception {
        List<Card> deckCards = new ArrayList<Card>();

        // set up db
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = AslDbHelper.getInstance(context);
        dbHelper.clearTables(dbHelper.getWritableDatabase());

        // create some mock video/card entries
        cardManager = ExternalCardManager.getInstance(context);
        Video video = mock(Video.class);
        when(video.getVideoId()).thenReturn(1);
        deckCards.add(cardManager.buildCard(video, "some answer"));
        Video video2 = mock(Video.class);
        when(video2.getVideoId()).thenReturn(2);
        deckCards.add(cardManager.buildCard(video2, "some answer"));

        // build deck
        deckManager = (ExternalDeckManager) ExternalDeckManager.getInstance(context);
        deck = deckManager.buildDeck("some deck", deckCards);

        // build test
        testManager = ExternalTestManager.getInstance(context);
        options = new TestManager.Options();
        options.recordStats = true;
        options.count = 10;
        options.mode = TestManager.OrderingMode.RANDOM;
        options.questionTypes = TestManager.Options.QUESTION_MULTIPLE_CHOICE;
        test = testManager.buildTest(Arrays.asList(deck), options);


    }

    @org.junit.Test
    public void takeTest() throws Exception {
        while(test.hasNext()) {
            Question question = test.next();
            assertEquals(question.getOptions().size(), 2);  // 2 cards, 1 deck
            Pair<Boolean, String> answer = question.answer("some answer");
            assertTrue(answer.first);
            assertEquals(answer.second, "some answer");
        }
        Statistics stats = ExternalStatisticsManager.getInstance(context).forDeck(deck);
        assertEquals(stats.getCorrect(), 2);
        assertEquals(stats.getIncorrect(), 0);
    }
}
