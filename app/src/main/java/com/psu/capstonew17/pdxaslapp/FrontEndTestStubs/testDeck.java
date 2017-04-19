package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brook on 4/17/2017.
 */

public class testDeck implements Deck {
    protected String name;
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Card> getCards() {
        List<Card> ret = new ArrayList<>();
        testCard first = new testCard();
        first.setAnswer("One Answer");
        ret.add(first);
        first = new testCard();
        first.setAnswer("Two Answer");
        ret.add(first);
        return ret;
    }

    @Override
    public void commit() {

    }

    @Override
    public void delete() {

    }

}
