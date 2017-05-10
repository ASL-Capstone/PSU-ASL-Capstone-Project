package com.psu.capstonew17.pdxaslapp;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.api.stubs.CardStub;
import com.psu.capstonew17.backend.api.stubs.DeckStub;
//import com.psu.capstonew17.backend.data.ExternalDeck;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import java.lang.reflect.GenericArrayType;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Tim on 5/6/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DecksInstrumentalTest {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void getDecks() {
        DeckManager deckManager = ExternalDeckManager.getInstance((context));
        List<Deck> deckList = deckManager.getDecks("shouldBeEmpty");
        assert(deckList.isEmpty());

        List<Card> cards = new ArrayList<Card>();
        deckList.add(new DeckStub());
        deckList.add(new DeckStub());
        try {
            Deck temp1 = deckManager.buildDeck("Name", cards);
            assertEquals(temp1.getName(), "Name");

            deckManager.buildDeck("deck1", cards);
            deckManager.buildDeck("deck2", cards);

            List<Deck> allDecks = deckManager.getDecks("null");
            assert(allDecks.size() == 2);
        } catch (ObjectAlreadyExistsException e){}
    }

    @Test
    public void buildDeck() {
        DeckManager deckManager = ExternalDeckManager.getInstance((context));
        List<Card> cards = new ArrayList<Card>();

        cards.add(new CardStub());
        cards.add(new CardStub());

        try {
            Deck temp1 = deckManager.buildDeck("first", cards);
            cards.add(new CardStub());
            Deck temp2 = deckManager.buildDeck("second", cards);
            assertEquals(temp1.getName(), "first");
            assertNotEquals(temp1.getName(), temp2.getName());
            assert(temp1.getCards().size() == 2);

        } catch (ObjectAlreadyExistsException e) {}
    }

    @Test
    public void getDefaultDeck() {
        DeckManager deckManager = ExternalDeckManager.getInstance((context));
        List<Card> cards = new ArrayList<Card>();
        cards.add(new CardStub());

        try {
            Deck temp1 = deckManager.buildDeck("first", cards);
            cards.add(new CardStub());
            cards.add(new CardStub());
            Deck temp2 = deckManager.buildDeck("second", cards);
            Deck allCards = deckManager.getDefaultDeck();
            assert(allCards.getCards().size() == 3);
        } catch (ObjectAlreadyExistsException e) {}
    }
}
