package com.psu.capstonew17.pdxaslapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.CardManager;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.db.AslDbContract;
import com.psu.capstonew17.backend.db.AslDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private Card mockCard;
    private Deck deck;
    private String name = UUID.randomUUID().toString();
    private List<Card> deckCards;
    private List<Integer> videoIds = Arrays.asList(-9999, -9998, -9997);

    @After
    public void cleanUpDB() {
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

    @Before
    public void setup() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        deckManager = (ExternalDeckManager) ExternalDeckManager.getInstance(context);
        dbHelper = AslDbHelper.getInstance(context);

        // create some mock video/card entries + deck to test with
        cardManager = ExternalCardManager.getInstance(context);
        deckCards = new ArrayList<Card>();
        Video video = mock(Video.class);
        when(video.getVideoId()).thenReturn(videoIds.get(0));
        deckCards.add(cardManager.buildCard(video, "some answer"));
        Video video2 = mock(Video.class);
        when(video2.getVideoId()).thenReturn(videoIds.get(1));
        deckCards.add(cardManager.buildCard(video2, "another answer"));
        mockCard = mock(Card.class);
        when(mockCard.getCardId()).thenReturn(-9999);
        deck = deckManager.buildDeck(name, deckCards);
    }

    @Test
    public void createNewDeck() throws Exception {
        String newDeckName = UUID.randomUUID().toString();
        String repeatCardsDeckName = UUID.randomUUID().toString();
        Deck newDeck = deckManager.buildDeck(newDeckName, Arrays.asList(mockCard));
        Deck repeatCardsDeck = deckManager.buildDeck(repeatCardsDeckName, Arrays.asList(mockCard, mockCard, mockCard));
        assertEquals(deckManager.getDeck(newDeckName).getCards().size(), 1);
        assertEquals(deckManager.getDeck(repeatCardsDeckName).getCards().size(), 3);
        newDeck.delete();
        repeatCardsDeck.delete();
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
        int numDecks = deckManager.getDecks(null).size();
        List<Deck> newDecks = new ArrayList<Deck>();
        int numNewDecks = 300;
        for(int i = 0; i < numNewDecks; i++){
            newDecks.add(deckManager.buildDeck(UUID.randomUUID().toString(), Arrays.asList(mockCard)));
        }
        assertEquals(deckManager.getDecks(null).size(), numDecks + numNewDecks);
        for(Deck d : newDecks){
            d.delete();
        }
        assertEquals(deckManager.getDecks(null).size(), numDecks);
    }

    @Test
    public void changeName() throws Exception {
        String anotherName = UUID.randomUUID().toString();
        assertEquals(deck.getName(), name);
        deck.setName(name);
        assertEquals(deck.getName(), name);
        deck.setName(anotherName);
        assertEquals(anotherName, deck.getName());
        String deck2Name = UUID.randomUUID().toString();
        Deck deck2 = deckManager.buildDeck(deck2Name, Arrays.asList(mock(Card.class)));
        try{
            deck.setName(deck2Name);
            deck2.delete();
            throw new AssertionError();
        } catch (ObjectAlreadyExistsException e){
            deck2.delete();
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
        when(video.getVideoId()).thenReturn(videoIds.get(2));
        Card card = cardManager.buildCard(video, "card three");
        cards.add(card);
        deck.commit();
        assertTrue(card.getUsers().contains(deck));
        deck.delete();
        assertNull(deckManager.getDeck(name));
        assertFalse(card.getUsers().contains(deck));
    }

}
