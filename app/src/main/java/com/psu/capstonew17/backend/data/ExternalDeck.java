package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.api.*;

import java.util.ArrayList;
import java.util.List;

public class ExternalDeck implements Deck {
    private int deckId;
    private String deckName;
    private List<Card> cards;

    private AslDbHelper dbHelper;

    public ExternalDeck(int deckId, String deckName, List<Card> cards){
        this.deckId = deckId;
        this.deckName = deckName;
        this.cards = new ArrayList<Card>(cards);
    }


    @Override
    public String getName() {
        return this.deckName;
    }

    @Override
    public void setName(String name) {
        this.deckName = name;
        dbHelper = ExternalDeckManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeckEntry.COLUMN_NAME, name);
        db.update(
                DeckEntry.TABLE_NAME, values,
                DeckEntry.COLUMN_ID + "=" + this.deckId, null
        );
    }

    public int getDeckId(){
        return  this.deckId;
    }

    @Override
    public List<Card> getCards() {
        return this.cards;
    }

    @Override
    public void commit() {
        removeCardsFromDeck();
        dbHelper = ExternalDeckManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(Card card: this.cards){
            ContentValues values = new ContentValues();
            values.put(RelationEntry.COLUMN_DECK, deckId);
            values.put(RelationEntry.COLUMN_CARD, ((ExternalCard) card).getId());
            db.insert(RelationEntry.TABLE_NAME, null, values);
        }
    }

    @Override
    public void delete() {
        removeCardsFromDeck();
        dbHelper = ExternalDeckManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                DeckEntry.TABLE_NAME,
                DeckEntry.COLUMN_ID + "=" + this.deckId, null
        );
    }

    private void removeCardsFromDeck(){
        dbHelper = ExternalDeckManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                RelationEntry.TABLE_NAME,
                RelationEntry.COLUMN_DECK + "=" + this.deckId, null
        );
    }
}
