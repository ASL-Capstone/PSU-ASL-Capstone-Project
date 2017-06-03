package com.psu.capstonew17.backend.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.db.AslDbContract;
import com.psu.capstonew17.backend.db.AslDbHelper;

import java.util.ArrayList;
import java.util.List;

public class ExternalDefaultDeck implements Deck {
    private int deckId = -1;
    private String deckName = "[[DEFAULT]]";
    private AslDbHelper dbHelper;

    @Override
    public String getName() {
        return this.deckName;
    }

    @Override
    public int getDeckId() {
        return deckId;
    }

    @Override
    public void setName(String name) throws ObjectAlreadyExistsException {
        return;
    }

    @Override
    public List<Card> getCards() {
        dbHelper = ExternalDeckManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.buildSelectQuery(AslDbContract.CardEntry.TABLE_NAME, null);
        Cursor cursor = db.rawQuery(query, null);
        List<Card> cards = new ArrayList<Card>();
        List<Integer> cardIds = new ArrayList<Integer>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(AslDbContract.CardEntry.COLUMN_ID));
            cardIds.add(id);
        }
        cursor.close();
        for(Integer i : cardIds){
            cards.add(ExternalCardManager.INSTANCE.getCard(i));
        }
        return cards;
    }

    @Override
    public void commit() {
        return;
    }

    @Override
    public void delete() {
        return;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ExternalDefaultDeck)) return false;
        return true;
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new ExternalDefaultDeck();
        }

        @Override
        public Object[] newArray(int i) {
            return new ExternalDefaultDeck[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
