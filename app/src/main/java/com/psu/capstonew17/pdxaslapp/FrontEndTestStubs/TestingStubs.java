package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;

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
        d.setName("Deck One");
        ret.add(d);
        return ret;
    }

    public static List<Deck> manyDecks(){
        Deck d = new testDeck();
        List<Deck> ret = new ArrayList<>();
        for(int i = 1; i < 21; ++i){
            d.setName("Deck " + i);
            ret.add(d);
            d = new testDeck();
        }
        d.setName("Deck Twenty-One");
        ret.add(d);
        d = new testDeck();
        d.setName("Deck Twenty-Two");
        ret.add(d);
        d = new testDeck();
        d.setName("Deck Twenty-Three");
        ret.add(d);
        return ret;
    }
}
