package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.ArrayList;

import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.db.AslDbContract.*;

public class ExternalDeckManager implements DeckManager{
    public static ExternalDeckManager INSTANCE = new ExternalDeckManager();

    private AslDbHelper dbHelper;

    public static DeckManager getInstance(Context context){
        INSTANCE.dbHelper = new AslDbHelper(context);
        return INSTANCE;
    }

    public AslDbHelper getDbHelper(){
        return dbHelper;
    }

    private List<Card> getCardsForDeck(int deckId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + RelationEntry.TABLE_NAME +
                " WHERE " + RelationEntry.COLUMN_DECK + "=" + deckId;
        Cursor cursor = db.rawQuery(query, null);
        List<Card> cards = new ArrayList<Card>();
        while(cursor.moveToNext()){
            int cardId = cursor.getInt(cursor.getColumnIndex(RelationEntry.COLUMN_CARD));
            cards.add(ExternalCardManager.INSTANCE.getCard(cardId));
        }
        return cards;
    }

    public Deck getDeck(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DeckEntry.TABLE_NAME +
                " WHERE " + DeckEntry.COLUMN_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            String deckName = cursor.getString(cursor.getColumnIndex(DeckEntry.COLUMN_NAME));
            return new ExternalDeck(id, deckName, getCardsForDeck(id));
        }
        return null;
    }


    @Override
    public List<Deck> getDecks(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Deck> decks = new ArrayList<Deck>();
        if(name == null){
            // get all decks
            String query = "SELECT * FROM " + DeckEntry.TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()){
                int deckId = cursor.getInt(cursor.getColumnIndex(DeckEntry.COLUMN_ID));
                decks.add(getDeck(deckId));
            }
        }
        //TODO: get list by name
        return decks;
    }

    @Override
    public Deck buildDeck(String name, List<Card> cards) throws ObjectAlreadyExistsException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeckEntry.COLUMN_NAME, name);
        try{
            int deckId = (int) db.insertOrThrow(DeckEntry.TABLE_NAME, null, values);
            for(Card card : cards){
                values = new ContentValues();
                values.put(RelationEntry.COLUMN_DECK, deckId);
                values.put(RelationEntry.COLUMN_CARD, ((ExternalCard) card).getId());
                db.insertOrThrow(RelationEntry.TABLE_NAME, null, values);
            }
            return new ExternalDeck(deckId, name, cards);
        } catch (SQLException e){
            throw new ObjectAlreadyExistsException(e);
        }
    }

    @Override
    public Deck getDefaultDeck() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CardEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        List<Card> cards = new ArrayList<Card>();
        while(cursor.moveToNext()){
            int cardId = cursor.getInt(cursor.getColumnIndex(CardEntry.COLUMN_ID));
            cards.add(ExternalCardManager.INSTANCE.getCard(cardId));
        }
        return new ExternalDeck(-1, "[[DEFAULT]]", cards);
    }
}
