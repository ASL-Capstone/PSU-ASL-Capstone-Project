package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalStatisticsManager;
import com.psu.capstonew17.backend.data.ExternalTestManager;
import com.psu.capstonew17.backend.db.AslDbContract;
import com.psu.capstonew17.backend.db.AslDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@RunWith(AndroidJUnit4.class)
public class TestInstrumentalTest {
    private Context context;
    private AslDbHelper dbHelper;
    private CardManager cardManager;
    private DeckManager deckManager;
    private TestManager testManager;
    private Deck deck;
    private String deckName = UUID.randomUUID().toString();
    private com.psu.capstonew17.backend.api.Test test;
    private TestManager.Options options;
    private String correctAnswer = "some answer";
    private String incorrectAnswer = "wrong answer";
    private Random random;
    private List<Integer> videoIds = Arrays.asList(-9999, -9998, -9997, -9996);

    @Before
    public void setup() throws Exception {
        List<Card> deckCards = new ArrayList<Card>();
        random = new Random();

        // set up db
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = AslDbHelper.getInstance(context);

        // create some mock video/card entries
        cardManager = ExternalCardManager.getInstance(context);
        Video video = mock(Video.class);
        when(video.getVideoId()).thenReturn(videoIds.get(0));
        deckCards.add(cardManager.buildCard(video, correctAnswer));
        Video video2 = mock(Video.class);
        when(video2.getVideoId()).thenReturn(videoIds.get(1));
        deckCards.add(cardManager.buildCard(video2, correctAnswer));
        Video video3 = mock(Video.class);
        when(video3.getVideoId()).thenReturn(videoIds.get(2));
        deckCards.add(cardManager.buildCard(video3, correctAnswer));
        Video video4 = mock(Video.class);
        when(video4.getVideoId()).thenReturn(videoIds.get(3));
        deckCards.add(cardManager.buildCard(video4, correctAnswer));

        // build deck
        deckManager = ExternalDeckManager.getInstance(context);
        deck = deckManager.buildDeck(deckName, deckCards);

        // build test
        testManager = ExternalTestManager.getInstance(context);
        options = new TestManager.Options();
        options.recordStats = true;
        options.count = 10;
        options.mode = TestManager.OrderingMode.RANDOM;
        options.questionTypes = TestManager.Options.QUESTION_MULTIPLE_CHOICE;
        test = testManager.buildTest(Arrays.asList(deck), options);
    }

    @After
    public void cleanUpDB(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(Integer id : videoIds) {
            db.delete(AslDbContract.CardEntry.TABLE_NAME,
                    AslDbContract.CardEntry.COLUMN_VIDEO + " = " + Integer.toString(id),
                    null
            );
        }
        if(deck != null) {
            deck.delete();
        }
    }

    @org.junit.Test
    public void takeTest() throws Exception {
        int numCorrect = 0;
        int numIncorrect = 0;
        while(test.hasNext()) {
            Question question = test.next();
            // edge case: one deck with 4 unique cards, all with the same answer. Option size should be 4
            assertEquals(question.getOptions().size(), 4);
            if(random.nextBoolean()) {
                Pair<Boolean, String> answer = question.answer(correctAnswer);
                assertTrue(answer.first);
                assertEquals(answer.second, correctAnswer);
                numCorrect += 1;
            }
            else{
                Pair<Boolean, String> answer = question.answer(incorrectAnswer);
                assertFalse(answer.first);
                assertEquals(answer.second, correctAnswer);
                numIncorrect += 1;
            }
            assertEquals(test.getStats().getCorrect(), numCorrect);
            assertEquals(test.getStats().getIncorrect(), numIncorrect);
        }
        Statistics stats = ExternalStatisticsManager.getInstance(context).forDeck(deck);
        assertEquals(stats.getCorrect(), numCorrect);
        assertEquals(stats.getIncorrect(), numIncorrect);
    }
}
