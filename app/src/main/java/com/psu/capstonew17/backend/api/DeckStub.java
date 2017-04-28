package com.psu.capstonew17.backend.api;

import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class DeckStub implements Deck{
    private String name;
    private List<Card> cards;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void commit() {
        //Updates the list
    }

    public void delete() {
        //Delete deck
    }
}
