package com.example.backendtesting.backend.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;

import com.example.backendtesting.backend.db.AslDbContract.*;
import com.example.backendtesting.backend.api.*;

import java.util.Date;


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
        long id = db.insert(VideoEntry.TABLE_NAME, null, values);
        Cursor cursor = db.rawQuery("select * from " + VideoEntry.TABLE_NAME + " where " +
            VideoEntry.COLUMN_ID + "=" + Long.toString(id), null);

        if(cursor != null){
            cursor.moveToFirst();
            int uid = cursor.getInt(cursor.getColumnIndex(VideoEntry.COLUMN_ID));
            String vPath = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_PATH));
            String vSha = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_SHA));
            cursor.close();
            return new Video(uid, vPath, vSha);
        }
        else{
            return null;
        }
    }

    public Deck insertDeck(String deckName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeckEntry.COLUMN_NAME, deckName);
        long id = db.insert(DeckEntry.TABLE_NAME, null, values);
        Cursor cursor = db.rawQuery("select * from " + DeckEntry.TABLE_NAME + " where " +
                DeckEntry.COLUMN_ID + "=" + Long.toString(id), null);

        if(cursor != null){
            cursor.moveToFirst();
            int uid = cursor.getInt(cursor.getColumnIndex(DeckEntry.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DeckEntry.COLUMN_NAME));
            cursor.close();
            return new Deck(uid, name);
        }
        else{
            return null;
        }
    }

    public Card insertCard(Video video, String answer){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_VIDEO, video.videoId);
        values.put(CardEntry.COLUMN_ANSWER, answer);
        long id = db.insert(CardEntry.TABLE_NAME, null, values);
        Cursor cursor = db.rawQuery("select * from " + CardEntry.TABLE_NAME + " where " +
                CardEntry.COLUMN_ID + "=" + Long.toString(id), null);

        if(cursor != null){
            cursor.moveToFirst();
            int uid = cursor.getInt(cursor.getColumnIndex(CardEntry.COLUMN_ID));
            int videoId = cursor.getInt(cursor.getColumnIndex(CardEntry.COLUMN_VIDEO));
            String ans = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_ANSWER));
            cursor.close();
            return new Card(uid, videoId, ans);
        }
        else{
            return null;
        }
    }

    public void insertAnswer(Date date, Card card, Deck deck, Answer.QuestionType type, Boolean correct){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnswerEntry.COLUMN_CREATED_AT, date.toString());
        values.put(AnswerEntry.COLUMN_CARD, card.cardId);
        values.put(AnswerEntry.COLUMN_DECK, deck.deckId);
        values.put(AnswerEntry.COLUMN_TYPE, type.ordinal());
        values.put(AnswerEntry.COLUMN_CORRECT, correct);
        db.insert(AnswerEntry.TABLE_NAME, null, values);
    }
    public void insertRelation(Card card, Deck deck){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RelationEntry.COLUMN_CARD, card.cardId);
        values.put(RelationEntry.COLUMN_DECK, deck.deckId);
        db.insert(RelationEntry.TABLE_NAME, null, values);
    }


}
