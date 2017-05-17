package com.psu.capstonew17.backend.api.stubs;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;

import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class DeckManagerStub implements DeckManager {

    private List<Deck> decks;
    private String name;
    private DeckStub defaultDeck;

    private class node {
        String name;
        List<Deck> deck;
        node next;
    }

    public List<Deck> getDecks(String name) {

        return decks;
    }

    public Deck buildDeck(String name, List<Card> cards) throws ObjectAlreadyExistsException {


        Deck temp = new DeckStub();

        temp.setName(name);

        return temp;
    }

    public Deck getDefaultDeck() {
        return defaultDeck;
    }
}
