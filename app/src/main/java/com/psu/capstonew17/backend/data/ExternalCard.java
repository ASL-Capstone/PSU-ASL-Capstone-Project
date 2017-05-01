//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.db.AslDbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ExternalCard implements Card {
    private int cardId;
    private Video video;
    private String answer;

    private AslDbHelper dbHelper;

    public ExternalCard(int id, Video video, String answer){
        this.cardId = id;
        this.video = video;
        this.answer = answer;
    }

    @Override
    public Video getVideo() {
        return video;
    }

    @Override
    public void setVideo(Video v) {
        this.video = v;
        dbHelper = ExternalCardManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_VIDEO, ((ExternalVideo) v).getVideoId());
        db.update(
                CardEntry.TABLE_NAME, values,
                CardEntry.COLUMN_ID + "=" + this.cardId, null
        );
    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(String a) {
        this.answer = a;
        dbHelper = ExternalCardManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_ANSWER, a);
        db.update(
                CardEntry.TABLE_NAME, values,
                CardEntry.COLUMN_ID + "=" + this.cardId, null
        );
    }

    public int getId() {
        return cardId;
    }

    @Override
    public void delete() throws ObjectInUseException {
        if(!getUsers().isEmpty()){
            throw new ObjectInUseException("Card is being used in a deck.");
        }
        // remove SQL
        dbHelper = ExternalCardManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                CardEntry.TABLE_NAME,
                CardEntry.COLUMN_ID + "=" + this.cardId, null
        );
    }

    @Override
    public List<Deck> getUsers() {
        dbHelper = ExternalCardManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.buildSelectQuery(
                RelationEntry.TABLE_NAME,
                Arrays.asList(RelationEntry.COLUMN_CARD + "=" + Integer.toString(this.cardId))
        );
        Cursor cursor = db.rawQuery(query, null);
        List<Deck> decks = new ArrayList<Deck>();
        while(cursor.moveToNext()){
            int deckId = cursor.getInt(cursor.getColumnIndex(RelationEntry.COLUMN_DECK));
            decks.add(ExternalDeckManager.INSTANCE.getDeck(deckId));
        }
        return decks;
    }
}
