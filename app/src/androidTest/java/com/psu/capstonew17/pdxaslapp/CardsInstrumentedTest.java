package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.api.ObjectInUseException;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.db.AslDbHelper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class CardsInstrumentedTest {

    private ExternalCardManager cardManager;
    private AslDbHelper dbHelper;
    private Context context;
    private Random random;
    private String answer = "some answer";
    private Video video;

    @Before
    public void setup() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        cardManager = (ExternalCardManager) ExternalCardManager.getInstance(context);
        dbHelper = AslDbHelper.getInstance(context);
        dbHelper.clearTables(dbHelper.getWritableDatabase());
        random = new Random();
        video = mock(Video.class);
        when(video.getVideoId()).thenReturn(random.nextInt(100));
    }

    @Test
    public void createNewCard() throws Exception {
        Card card = cardManager.buildCard(video, answer);
        Card expected = cardManager.getCard(card.getCardId());
        assertEquals(expected.getAnswer(), answer);
    }

    @Test
    public void createExistingCard() throws Exception {
        cardManager.buildCard(video, answer);
        try{
            cardManager.buildCard(video, "another answer");
            throw new AssertionError();
        } catch (ObjectAlreadyExistsException e){
            assert true;
        }
    }

    @Test
    public void changeAnswer() throws Exception {
        Card card = cardManager.buildCard(video, answer);
        assertEquals(card.getAnswer(), answer);
        card.setAnswer("another answer");
        assertEquals(cardManager.getCard(card.getCardId()).getAnswer(), "another answer");
    }

    @Test
    public void deleteCardInUse() throws Exception {
        Card card = cardManager.buildCard(video, answer);
        DeckManager deckManager = ExternalDeckManager.getInstance(context);
        deckManager.buildDeck("some deck", Arrays.asList(card));
        try{
            card.delete();
            throw new AssertionError();
        } catch (ObjectInUseException e){
            assert true;
        }
    }

    @Test
    public void getUsers() throws Exception {
        Card card = cardManager.buildCard(video, answer);
        assertTrue(card.getUsers().isEmpty());
        DeckManager deckManager = ExternalDeckManager.getInstance(context);
        deckManager.buildDeck("some deck", Arrays.asList(card));
        deckManager.buildDeck("another deck", Arrays.asList(card));
        Deck deck3 = deckManager.buildDeck("third deck", new ArrayList<Card>());
        deck3.getCards().add(card);
        List<String> userNames = new ArrayList<String>();
        for(Deck d : card.getUsers()){
            userNames.add(d.getName());
        }
        assertTrue(userNames.contains("some deck"));
        assertTrue(userNames.contains("another deck"));
        assertFalse(userNames.contains("third deck"));
        deck3.commit();
        for(Deck d : card.getUsers()){
            userNames.add(d.getName());
        }
        assertTrue(userNames.contains("third deck"));
    }

}
