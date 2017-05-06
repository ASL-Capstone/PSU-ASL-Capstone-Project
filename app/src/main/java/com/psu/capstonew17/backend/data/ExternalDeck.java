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
    private List<Card> mutableCards, dbCards;

    private AslDbHelper dbHelper;

    public ExternalDeck(int deckId, String deckName, List<Card> cards){
        this.deckId = deckId;
        this.deckName = deckName;
        this.dbCards = cards;
        this.mutableCards = new ArrayList<Card>(dbCards);
    }


    @Override
    public String getName() {
        return this.deckName;
    }

    @Override
    public void setName(String name) throws ObjectAlreadyExistsException {
        if(ExternalDeckManager.INSTANCE.deckExists(name)){
            throw new ObjectAlreadyExistsException("The deck '" + name + "' already exists.");
        }
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
        return this.mutableCards;
    }

    @Override
    public void commit() {
        dbHelper = ExternalDeckManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // remove deleted cards
        String whereArgs[] = new String[2];
        whereArgs[0] = String.valueOf(deckId);

        for(Card card: this.dbCards) {
            if(!mutableCards.contains(card)) {
                whereArgs[1] = String.valueOf(((ExternalCard)card).getId());

                db.delete(RelationEntry.TABLE_NAME,
                        String.format("%s = ? AND %s = ?",
                                RelationEntry.COLUMN_DECK,
                                RelationEntry.COLUMN_CARD),
                        whereArgs);
            }
        }

        // add inserted cards
        for(Card card : mutableCards) {
            if(!dbCards.contains(card)) {
                ContentValues values = new ContentValues();
                values.put(RelationEntry.COLUMN_DECK, deckId);
                values.put(RelationEntry.COLUMN_CARD, ((ExternalCard) card).getId());
                db.insert(RelationEntry.TABLE_NAME, null, values);
            }
        }

        // update lists
        dbCards.clear();
        dbCards.addAll(mutableCards);
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ExternalDeck)) return false;
        return deckId == ((ExternalDeck)obj).deckId;
    }
}
