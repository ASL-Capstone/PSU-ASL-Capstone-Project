package com.example.backendtesting.backend.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import com.example.backendtesting.backend.data.Answer;
import com.example.backendtesting.backend.db.AslDbContract.*;
import com.example.backendtesting.backend.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AslDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_CARD_TABLE =
            "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                    CardEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    CardEntry.COLUMN_VIDEO + " INTEGER," +
                    CardEntry.COLUMN_ANSWER + " TEXT," +
                    "FOREIGN KEY(" + CardEntry.COLUMN_VIDEO + ") " +
                    "REFERENCES " + VideoEntry.TABLE_NAME + "(" + VideoEntry.COLUMN_ID + "))";

    private static final String SQL_CREATE_VIDEO_TABLE =
            "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                    VideoEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    VideoEntry.COLUMN_PATH + " TEXT," +
                    VideoEntry.COLUMN_SHA + " TEXT)";

    private static final String SQL_CREATE_ANSWER_TABLE =
            "CREATE TABLE " + AnswerEntry.TABLE_NAME + " (" +
                    AnswerEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    AnswerEntry.COLUMN_CREATED_AT + " TEXT," +
                    AnswerEntry.COLUMN_CARD + " INTEGER," +
                    AnswerEntry.COLUMN_DECK + " INTEGER," +
                    AnswerEntry.COLUMN_TYPE + " INTEGER," +
                    AnswerEntry.COLUMN_CORRECT + " BOOLEAN," +
                    "FOREIGN KEY(" + AnswerEntry.COLUMN_CARD + ") " +
                    "REFERENCES " + CardEntry.TABLE_NAME + "(" + CardEntry.COLUMN_ID + ")," +
                    "FOREIGN KEY(" + AnswerEntry.COLUMN_DECK + ") " +
                    "REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry.COLUMN_ID + "))";

    private static final String SQL_CREATE_DECK_TABLE =
            "CREATE TABLE " + DeckEntry.TABLE_NAME + " (" +
                    DeckEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    DeckEntry.COLUMN_NAME + " TEXT)";

    private static final String SQL_CREATE_RELATION_TABLE =
            "CREATE TABLE " + RelationEntry.TABLE_NAME + " (" +
                    RelationEntry.COLUMN_CARD + " INTEGER," +
                    RelationEntry.COLUMN_DECK + " INTEGER," +
                    "FOREIGN KEY(" + RelationEntry.COLUMN_CARD + ") " +
                    "REFERENCES " + CardEntry.TABLE_NAME + "(" + CardEntry.COLUMN_ID + ")," +
                    "FOREIGN KEY(" + RelationEntry.COLUMN_DECK + ") " +
                    "REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry.COLUMN_ID + "))";

    private static final String SQL_DROP_CARD =
            "DROP TABLE IF EXISTS " + CardEntry.TABLE_NAME;

    private static final String SQL_DROP_VIDEO =
            "DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME;

    private static final String SQL_DROP_ANSWER =
            "DROP TABLE IF EXISTS " + AnswerEntry.TABLE_NAME;

    private  static final String SQL_DROP_DECK =
            "DROP TABLE IF EXISTS " + DeckEntry.TABLE_NAME;

    private static final String SQL_DROP_RELATION =
            "DROP TABLE IF EXISTS " + RelationEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "asl.db";

    public AslDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        createTables(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public void createTables(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
        db.execSQL(SQL_CREATE_CARD_TABLE);
        db.execSQL(SQL_CREATE_DECK_TABLE);
        db.execSQL(SQL_CREATE_ANSWER_TABLE);
        db.execSQL(SQL_CREATE_RELATION_TABLE);
    }

    public void dropTables(SQLiteDatabase db){
        db.execSQL(SQL_DROP_VIDEO);
        db.execSQL(SQL_DROP_DECK);
        db.execSQL(SQL_DROP_CARD);
        db.execSQL(SQL_DROP_ANSWER);
        db.execSQL(SQL_DROP_RELATION);
    }

    public Video insertVideo(String path, String sha){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VideoEntry.COLUMN_PATH, path);
        values.put(VideoEntry.COLUMN_SHA, sha);
        try {
            int id = (int) db.insertOrThrow(VideoEntry.TABLE_NAME, null, values);
            return new Video(id, path, sha);
        } catch (SQLException e){
            Log.println(Log.DEBUG, "sql exception", e.toString());
            return null;
        }
    }

    public Deck insertDeck(String deckName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeckEntry.COLUMN_NAME, deckName);
        try {
            int id = (int) db.insertOrThrow(DeckEntry.TABLE_NAME, null, values);
            return new Deck(id ,deckName);
        } catch (SQLException e){
            Log.println(Log.DEBUG, "sql exception", e.toString());
            return null;
        }
    }

    public Card insertCard(Video video, String answer){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_VIDEO, video.videoId);
        values.put(CardEntry.COLUMN_ANSWER, answer);
        try {
            int id = (int) db.insertOrThrow(CardEntry.TABLE_NAME, null, values);
            return new Card(id, video.videoId, answer);
        } catch (SQLException e){
            Log.println(Log.DEBUG, "sql exception", e.toString());
            return null;
        }
    }

    public Answer insertAnswer(Date date, Card card, Deck deck, Answer.QuestionType type, Boolean correct){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnswerEntry.COLUMN_CREATED_AT, date.toString());
        values.put(AnswerEntry.COLUMN_CARD, card.cardId);
        values.put(AnswerEntry.COLUMN_DECK, deck.deckId);
        values.put(AnswerEntry.COLUMN_TYPE, type.ordinal());
        values.put(AnswerEntry.COLUMN_CORRECT, correct);
        try {
            int id = (int) db.insertOrThrow(AnswerEntry.TABLE_NAME, null, values);
            return new Answer(id, date.toString(), card.cardId, deck.deckId, type, correct);
        } catch (SQLException e){
            Log.println(Log.DEBUG, "sql exception", e.toString());
            return null;
        }
    }

    public void insertRelation(Card card, Deck deck){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RelationEntry.COLUMN_DECK, deck.deckId);
        values.put(RelationEntry.COLUMN_CARD, card.cardId);
        try {
            db.insertOrThrow(RelationEntry.TABLE_NAME, null, values);
        } catch (SQLException e){
            Log.println(Log.DEBUG, "sql exception", e.toString());
        }

    }

    public Deck findDeck(Integer deckId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DeckEntry.TABLE_NAME + " where " +
                DeckEntry.COLUMN_ID + "=" + Integer.toString(deckId), null
        );
        if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0){
            String name = cursor.getString(cursor.getColumnIndex(DeckEntry.COLUMN_NAME));
            cursor.close();
            return new Deck(deckId, name);
        }
        return null;
    }

    public Deck findDeck(String deckName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DeckEntry.TABLE_NAME + " where " +
            DeckEntry.COLUMN_NAME + "='" + deckName + "'", null
        );
        if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0){
            int uid = cursor.getInt(cursor.getColumnIndex(DeckEntry.COLUMN_ID));
            cursor.close();
            return new Deck(uid, deckName);
        }
        return null;
    }

    public Card findCard(Integer cardId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CardEntry.TABLE_NAME + " where " +
            CardEntry.COLUMN_ID + "=" + Integer.toString(cardId), null
        );
        if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0){
            int videoId = cursor.getInt(cursor.getColumnIndex(CardEntry.COLUMN_VIDEO));
            String answer = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_ANSWER));
            cursor.close();
            return new Card(cardId, videoId, answer);
        }
        return null;
    }

    public Card findCard(Video video, String answer){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CardEntry.TABLE_NAME + " where " +
            CardEntry.COLUMN_VIDEO + "=" + video.videoId + " and " + CardEntry.COLUMN_ANSWER +
                "='" + answer + "'" , null
        );
        if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0){
            int cardId = cursor.getInt(cursor.getColumnIndex(CardEntry.COLUMN_ID));
            cursor.close();
            return new Card(cardId, video.videoId, answer);
        }
        return null;
    }

    public Video findVideo(String videoSha){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + VideoEntry.TABLE_NAME + " where " +
            VideoEntry.COLUMN_SHA + "='" + videoSha + "'", null);
        if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0){
            int videoId = cursor.getInt(cursor.getColumnIndex(VideoEntry.COLUMN_ID));
            String videoPath = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_PATH));
            cursor.close();
            return new Video(videoId, videoPath, videoSha);
        }
        return null;
    }

    public boolean relationExists(Card card, Deck deck){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + RelationEntry.TABLE_NAME + " where " +
            RelationEntry.COLUMN_CARD + "=" + card.cardId + " and " + RelationEntry.COLUMN_DECK +
                "=" + deck.deckId, null
        );
        if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    public List<Card> getCardsInDeck(Deck deck){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + RelationEntry.TABLE_NAME + " where " +
                RelationEntry.COLUMN_DECK + " = " + Integer.toString(deck.deckId), null);
        if(cursor == null){
            return null;
        }
        List<Card> cards = new ArrayList<Card>();
        while(cursor.moveToNext()){
            int cardId = cursor.getInt(cursor.getColumnIndex(RelationEntry.COLUMN_CARD));
            cards.add(findCard(cardId));
        }
        cursor.close();
        return cards;
    }

    public List<Deck> getDecksUsingCard(Card card){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + RelationEntry.TABLE_NAME + " where " +
            RelationEntry.COLUMN_CARD + "=" + Integer.toString(card.cardId), null);
        if(cursor == null){
            return null;
        }
        List<Deck> decks = new ArrayList<Deck>();
        while(cursor.moveToNext()){
            int deckId = cursor.getInt(cursor.getColumnIndex(RelationEntry.COLUMN_DECK));
            decks.add(findDeck(deckId));
        }
        cursor.close();
        return decks;
    }

    public void removeCardFromDeck(Card card, Deck deck){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + RelationEntry.TABLE_NAME + " where " +
                RelationEntry.COLUMN_CARD + "=" + Integer.toString(card.cardId) + " and " +
                RelationEntry.COLUMN_DECK + "=" + Integer.toString(deck.deckId)
        );
    }

    public void removeDeck(Deck deck){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + DeckEntry.TABLE_NAME + " where " +
            DeckEntry.COLUMN_ID + "=" + Integer.toString(deck.deckId)
        );
    }

    public void removeCard(Card card){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + CardEntry.TABLE_NAME + " where " +
            CardEntry.COLUMN_ID + "=" + Integer.toString(card.cardId)
        );
    }

    public void removeAnswer(Card card){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + AnswerEntry.TABLE_NAME + " where " +
            AnswerEntry.COLUMN_CARD + "=" + Integer.toString(card.cardId)
        );
    }

    public void removeAnswer(Card card, Deck deck){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + AnswerEntry.TABLE_NAME + " where " +
            AnswerEntry.COLUMN_DECK + "=" + Integer.toString(deck.deckId) + " and " +
            AnswerEntry.COLUMN_CARD + "=" + Integer.toString(card.cardId)
        );
    }
}
