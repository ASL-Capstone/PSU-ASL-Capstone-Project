package com.example.backendtesting.backend.api;

import android.content.Context;
import android.provider.ContactsContract;

import com.example.backendtesting.backend.db.AslDbHelper;
import java.util.List;


public class DeckManager {
    private AslDbHelper dbHelper;

    public DeckManager(Context context){
        dbHelper = new AslDbHelper(context);
    }

    public Deck createDeck(String name){
        Deck deck = dbHelper.findDeck(name);
        if(deck == null){
            return dbHelper.insertDeck(name);
        }
        return deck;
    }

    public Card createCard(Video video, String answer){
        Deck defaultDeck = dbHelper.findDeck("default");
        return createCard(video, answer, defaultDeck);
    }

    public Card createCard(Video video, String answer, Deck deck){
        Card card = dbHelper.findCard(video, answer);
        if(card == null){
            card = dbHelper.insertCard(video, answer);
        }
        addCardToDeck(card, deck);
        return card;
    }

    public void addCardToDeck(Card card, Deck deck){
        boolean exists = dbHelper.relationExists(card, deck);
        if(!exists) {
            dbHelper.insertRelation(card, deck);
        }
    }

    public List<Card> getCardsInDeck(Deck deck){
        return dbHelper.getCardsInDeck(deck);
    }

    public void removeCardFromDeck(Card card, Deck deck){
        dbHelper.removeCardFromDeck(card, deck);
    }

    public void deleteDeck(Deck deck){
        List<Card> cardsInDeck = getCardsInDeck(deck);
        Card card;
        for(int i = 0; i < cardsInDeck.size(); ++i){
            card = cardsInDeck.get(i);
            removeCardFromDeck(card, deck);
            dbHelper.removeAnswer(card, deck);
        }
        dbHelper.removeDeck(deck);
    }

    public void deleteCard(Card card){
        dbHelper.removeAnswer(card);
        List<Deck> decksUsingCard = dbHelper.getDecksUsingCard(card);
        Deck deck;
        for(int i = 0; i < decksUsingCard.size(); ++i){
            deck = decksUsingCard.get(i);
            removeCardFromDeck(card, deck);
            dbHelper.removeAnswer(card, deck);
        }
        dbHelper.removeCard(card);
    }
}
