package com.psu.capstonew17.backend.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        while(cursor.moveToNext()){
            int cardId = cursor.getInt(cursor.getColumnIndex(AslDbContract.CardEntry.COLUMN_ID));
            cards.add(ExternalCardManager.INSTANCE.getCard(cardId));
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
}
