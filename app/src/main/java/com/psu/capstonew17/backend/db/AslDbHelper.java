//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Pair;
import java.util.List;

import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.db.AslDbContract.*;

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
                    AnswerEntry.COLUMN_ASKED_AT + " INTEGER," +
                    AnswerEntry.COLUMN_ANSWERED_AT + " INTEGER," +
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
    public static AslDbHelper INSTANCE;

    public static AslDbHelper getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new AslDbHelper(context);
        }
        return INSTANCE;
    }
    private AslDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        dropTables(db);
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

    public void clearTables(SQLiteDatabase db){
        db.execSQL("DELETE FROM " + VideoEntry.TABLE_NAME);
        db.execSQL("DELETE FROM " + CardEntry.TABLE_NAME);
        db.execSQL("DELETE FROM " + DeckEntry.TABLE_NAME);
        db.execSQL("DELETE FROM " + AnswerEntry.TABLE_NAME);
        db.execSQL("DELETE FROM " + RelationEntry.TABLE_NAME);
    }

    public String buildSelectQuery(String tableName, List<String> whereConditions){
        String query =  "SELECT * FROM " + tableName;
        if(whereConditions == null){
            return query;
        }
        query += " WHERE ";
        for(String clause : whereConditions){
            query += clause;
            if(whereConditions.indexOf(clause) + 1 < whereConditions.size()){
                query += " AND ";
            }
        }
        return query;
    }

}
