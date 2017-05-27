package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brook on 4/17/2017.
 */

public class TestingStubs {
    public static List<Card> nullCards(){
        return null;
    }

    public static List<Card> oneCard(){
        Card one = new testCard();
        List<Card> ret = new ArrayList<>();
        one.setAnswer("One card's answer");
        ret.add(one);
        return ret;
    }

    public static List<Card> manyCards(){
        Card c = new testCard();
        List<Card> ret = new ArrayList<>();
        c.setAnswer("Card One");
        ret.add(c);
        c = new testCard();
        c.setAnswer("Card Two");
        ret.add(c);
        c = new testCard();
        c.setAnswer("Card Three");
        ret.add(c);
        return ret;
    }

    public static List<Deck> nullDecks(){
        return null;
    }

    public static List<Deck> oneDeck(){
       List<Deck> ret = new ArrayList<>();
       Deck d = new testDeck();
        try {
            d.setName("Deck One");
        } catch (ObjectAlreadyExistsException e){}
        ret.add(d);
        return ret;
    }

    public static List<Deck> manyDecks(){
        Deck d = new testDeck();
        List<Deck> ret = new ArrayList<>();
        try {
            d.setName("Deck One");
            ret.add(d);
            d = new testDeck();
            d.setName("Deck Two");
            ret.add(d);
            d = new testDeck();
            d.setName("Deck Three");
            ret.add(d);

            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);
            d.setName("Deck Three");
            ret.add(d);




        } catch (ObjectAlreadyExistsException e){}
        return ret;
    }
}
