package com.example.backendtesting.backend.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.example.backendtesting.backend.db.AslDbContract.*;


public class AslDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_CARD_TABLE =
            "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                    CardEntry.COLUMN_SHA + " TEXT PRIMARY KEY," +
                    CardEntry.COLUMN_PATH + " TEXT," +
                    CardEntry.COLUMN_ANSWER + " TEXT)";

    private static final String SQL_CREATE_DECK_TABLE =
            "CREATE TABLE " + DeckEntry.TABLE_NAME + " (" +
                    DeckEntry.COLUMN_NAME + " TEXT PRIMARY KEY)";

    private static final String SQL_CREATE_RELATION_TABLE =
            "CREATE TABLE " + RelationEntry.TABLE_NAME + " (" +
                    RelationEntry.COLUMN_DECK_NAME + " TEXT," +
                    RelationEntry.COLUMN_VIDEO_SHA + " TEXT," +
                    "FOREIGN KEY(" + RelationEntry.COLUMN_DECK_NAME + ") " +
                    "REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry.COLUMN_NAME + ")," +
                    "FOREIGN KEY(" + RelationEntry.COLUMN_VIDEO_SHA + ") " +
                    "REFERENCES " + CardEntry.TABLE_NAME + "(" + CardEntry.COLUMN_SHA + "))";

    private static final String SQL_DROP_CARD =
            "DROP TABLE IF EXISTS " + CardEntry.TABLE_NAME;

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
        db.execSQL(SQL_CREATE_CARD_TABLE);
        db.execSQL(SQL_CREATE_DECK_TABLE);
        db.execSQL(SQL_CREATE_RELATION_TABLE);
    }

    public void dropTables(SQLiteDatabase db){
        db.execSQL(SQL_DROP_CARD);
        db.execSQL(SQL_DROP_DECK);
        db.execSQL(SQL_DROP_RELATION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}
