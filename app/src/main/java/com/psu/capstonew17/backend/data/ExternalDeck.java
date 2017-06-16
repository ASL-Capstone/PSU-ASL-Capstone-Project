//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.psu.capstonew17.backend.EncodeableObject;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.api.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class ExternalDeck implements Deck, EncodeableObject {
    private int deckId;
    private String deckName;
    private List<Card> mutableCards, dbCards;

    private AslDbHelper dbHelper;

    private ExternalDeck(int deckId, String deckName, List<Card> db, List<Card> mutable){
        this.deckId = deckId;
        this.deckName = deckName;
        this.dbCards = db;
        this.mutableCards = mutable;
    }

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
        if(this.deckName.equals(name)){
            return;
        }
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

    @Override
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
                whereArgs[1] = String.valueOf((card).getCardId());

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
                values.put(RelationEntry.COLUMN_CARD, card.getCardId());
                db.insert(RelationEntry.TABLE_NAME, null, values);
            }
        }

        // update lists
        dbCards.clear();
        dbCards.addAll(mutableCards);
    }

    @Override
    public void delete() {
        dbHelper = ExternalDeckManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // remove references to deck
        db.delete(
                DeckEntry.TABLE_NAME,
                DeckEntry.COLUMN_ID + "=" + this.deckId, null
        );
        db.delete(
                RelationEntry.TABLE_NAME,
                RelationEntry.COLUMN_DECK + "=" + this.deckId, null
        );
        db.delete(
                AnswerEntry.TABLE_NAME,
                AnswerEntry.COLUMN_DECK + "=" + this.deckId, null
        );
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ExternalDeck)) return false;
        return deckId == ((ExternalDeck)obj).deckId;
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            int id = parcel.readInt();
            String name = parcel.readString();
            List<Card> mutable = new ArrayList<Card>();
            parcel.readTypedList(mutable, ExternalCard.CREATOR);
            List<Card> db = new ArrayList<Card>();
            parcel.readTypedList(db, ExternalCard.CREATOR);

            return new ExternalDeck(id, name, db, mutable);
        }

        @Override
        public Object[] newArray(int i) {
            return new ExternalDeck[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(deckId);
        parcel.writeString(deckName);
        parcel.writeTypedList(mutableCards);
        parcel.writeTypedList(dbCards);
    }

    @Override
    public byte[] encodeToByteArray() throws IOException {
        byte [] nameBytes = this.deckName.getBytes();
        int numCards = this.mutableCards.size();
        ByteBuffer b = ByteBuffer.allocate(12 + nameBytes.length + (numCards*4));
        b.putInt(this.deckId);
        b.putInt(nameBytes.length);
        b.put(nameBytes);
        b.putInt(numCards);
        for(Card c : this.mutableCards){
            b.putInt(c.getCardId());
        }
        return b.array();
    }
}
