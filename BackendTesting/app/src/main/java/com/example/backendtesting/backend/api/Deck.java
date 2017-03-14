package com.example.backendtesting.backend.api;

import android.content.Context;

import com.example.backendtesting.backend.db.AslDbHelper;

public class Deck {
    public int deckId;
    public String deckName;

    public Deck(int id, String name){
        deckId = id;
        deckName = name;
    }

    public static Deck createDeck(String name, Context context){
        AslDbHelper dbHelper = new AslDbHelper(context);
        // TODO: check if deck already exists
        return dbHelper.insertDeck(name);
    }

    public void addCardToDeck(Card card, Context context){
        AslDbHelper dbHelper = new AslDbHelper(context);
        dbHelper.insertRelation(card, this);
    }

    //TODO: get all cards in a deck
    public Card[] getCardsInDeck(Context context){
        AslDbHelper dbHelper = new AslDbHelper(context);

        return null;
    }
}
