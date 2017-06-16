package com.psu.capstonew17.pdxaslapp;


import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.CardManager;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.db.AslDbHelper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DecksInstrumentedTest {
    private ExternalDeckManager deckManager;
    private CardManager cardManager;
    private AslDbHelper dbHelper;
    private Context context;
    private Random random;
    private Card mockCard;
    private Deck deck;
    private String name = "some deck";

    @Before
    public void setup() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        deckManager = (ExternalDeckManager) ExternalDeckManager.getInstance(context);
        dbHelper = AslDbHelper.getInstance(context);
        dbHelper.clearTables(dbHelper.getWritableDatabase());
        random = new Random();

        // create some mock video/card entries + deck to test with
        cardManager = ExternalCardManager.getInstance(context);
        List<Card> deckCards = new ArrayList<Card>();
        Video video = mock(Video.class);
        when(video.getVideoId()).thenReturn(1);
        deckCards.add(cardManager.buildCard(video, "some answer"));
        Video video2 = mock(Video.class);
        when(video2.getVideoId()).thenReturn(2);
        deckCards.add(cardManager.buildCard(video2, "another answer"));
        mockCard = mock(Card.class);
        when(mockCard.getCardId()).thenReturn(random.nextInt(100));
        deck = deckManager.buildDeck(name, deckCards);
    }

    @Test
    public void createNewDeck() throws Exception {
        deckManager.buildDeck("a new deck", Arrays.asList(mockCard));
        deckManager.buildDeck("repeat cards", Arrays.asList(mockCard, mockCard, mockCard));
        assertEquals(deckManager.getDeck("a new deck").getCards().size(), 1);
        assertEquals(deckManager.getDeck("repeat cards").getCards().size(), 3);
    }

    @Test
    public void createExistingDeck() throws Exception {
        try{
            deckManager.buildDeck(name, Arrays.asList(mockCard, mockCard, mockCard));
            throw new AssertionError();
        } catch (ObjectAlreadyExistsException e){
            assert true;
        }
    }

    @Test
    public void createManyDecks() throws Exception {
        deck.delete();
        int numDecks = 300;
        for(int i = 0; i < numDecks; i++){
            deckManager.buildDeck(UUID.randomUUID().toString(), Arrays.asList(mockCard));
        }
        assertEquals(deckManager.getDecks(null).size(), numDecks);
    }

    @Test
    public void changeName() throws Exception {
        assertEquals(deck.getName(), name);
        deck.setName(name);
        assertEquals(deck.getName(), name);
        deck.setName("another name");
        assertEquals("another name", deck.getName());
        deckManager.buildDeck("deck two", Arrays.asList(mock(Card.class)));
        try{
            deck.setName("deck two");
            throw new AssertionError();
        } catch (ObjectAlreadyExistsException e){
            assert true;
        }
    }

    @Test
    public void modifyCards() throws Exception {
        List<Card> cards = deck.getCards();
        assertEquals(cards.size(), 2);
        cards.add(mockCard);
        assertEquals(deckManager.getDeck(name).getCards().size(), 2);
        deck.commit();
        assertEquals(deckManager.getDeck(name).getCards().size(), 3);
        cards.get(0).setAnswer("the right answer");
        assertEquals(deckManager.getDeck(name).getCards().get(0).getAnswer(), "the right answer");
        cards.clear();
        assertEquals(deckManager.getDeck(name).getCards().size(), 3);
        deck.commit();
        assertEquals(deckManager.getDeck(name).getCards().size(), 0);
    }

    @Test
    public void deleteDeck() throws Exception {
        List<Card> cards = deck.getCards();
        Video video = mock(Video.class);
        when(video.getVideoId()).thenReturn(3);
        Card card = cardManager.buildCard(video, "card three");
        cards.add(card);
        deck.commit();
        assertTrue(card.getUsers().contains(deck));
        deck.delete();
        assertNull(deckManager.getDeck(name));
        assertFalse(card.getUsers().contains(deck));
    }

}
