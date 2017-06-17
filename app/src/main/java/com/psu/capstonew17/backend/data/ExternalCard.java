//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.psu.capstonew17.backend.EncodeableObject;
import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.db.AslDbHelper;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class ExternalCard implements Card, EncodeableObject {
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
        values.put(CardEntry.COLUMN_VIDEO, v.getVideoId());
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

    @Override
    public int getCardId() {
        return cardId;
    }

    @Override
    public void delete() throws ObjectInUseException {
        if(!getUsers().isEmpty()){
            throw new ObjectInUseException("Card is being used in a deck.");
        }
        // remove references to card
        dbHelper = ExternalCardManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                CardEntry.TABLE_NAME,
                CardEntry.COLUMN_ID + "=" + this.cardId, null
        );
        db.delete(
                AnswerEntry.TABLE_NAME,
                AnswerEntry.COLUMN_CARD + "=" + this.cardId, null
        );
        // are there any more cards using this video?
        String query = dbHelper.buildSelectQuery(
                CardEntry.TABLE_NAME,
                Arrays.asList(CardEntry.COLUMN_VIDEO + "=" + this.video.getVideoId())
        );
        Cursor cursor = db.rawQuery(query, null);
        if(!cursor.moveToFirst()){
            // no longer in use
            File vFile = new File(((ExternalVideo) this.video).getVideoPath());
            if(vFile.exists()){
                vFile.delete();
            }
        }
        cursor.close();
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
        List<Integer> deckIds = new ArrayList<Integer>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(RelationEntry.COLUMN_DECK));
            deckIds.add(id);
        }
        cursor.close();

        for(Integer i : deckIds){
            decks.add(ExternalDeckManager.INSTANCE.getDeck(i));
        }
        return decks;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ExternalCard)) return false;
        return cardId == ((ExternalCard)obj).cardId;
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            int id = parcel.readInt();
            Video vid = (Video)parcel.readTypedObject(ExternalVideo.CREATOR);
            String answer = parcel.readString();
            return new ExternalCard(id, vid, answer);
        }

        @Override
        public Object[] newArray(int i) {
            return new ExternalCard[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(cardId);
        parcel.writeTypedObject(video, 0);
        parcel.writeString(answer);
    }

    @Override
    public byte[] encodeToByteArray() throws IOException {
        byte [] answerBytes = this.answer.getBytes();
        byte [] videoBytes = ((EncodeableObject) this.video).encodeToByteArray();
        ByteBuffer b = ByteBuffer.allocate(12 + answerBytes.length + videoBytes.length);
        b.putInt(this.cardId);
        b.putInt(answerBytes.length);
        b.put(answerBytes);
        b.putInt(videoBytes.length);
        b.put(videoBytes);
        return b.array();
    }
}
