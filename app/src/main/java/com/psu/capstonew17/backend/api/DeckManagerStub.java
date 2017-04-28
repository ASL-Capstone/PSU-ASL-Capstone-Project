package com.psu.capstonew17.backend.api;

import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class DeckManagerStub implements DeckManager {

    private List<Deck> decks;
    private String name;
    private DeckStub defaultDeck;

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
