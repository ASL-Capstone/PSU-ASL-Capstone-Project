package com.psu.capstonew17.backend.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbContract;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.db.AslDbContract.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ExternalStatisticsManager implements StatisticsManager {

    public static ExternalStatisticsManager INSTANCE = new ExternalStatisticsManager();

    private AslDbHelper dbHelper;

    public static StatisticsManager getInstance(Context context){
        INSTANCE.dbHelper = new AslDbHelper(context);
        return INSTANCE;
    }

    public AslDbHelper getDbHelper(){
        return this.dbHelper;
    }

    @Override
    public Statistics forTimeSpan(long start, long length) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.buildSelectQuery(
                AnswerEntry.TABLE_NAME,
                Arrays.asList(AnswerEntry.COLUMN_ASKED_AT + ">=" + String.valueOf(start),
                        AnswerEntry.COLUMN_ASKED_AT + "<=" + String.valueOf(start + length))
        );
        Cursor cursor = db.rawQuery(query, null);
        List<Card> correctCards = new ArrayList<Card>();
        List<Card> incorrectCards = new ArrayList<Card>();
        List<Long> times = new ArrayList<Long>();
        while(cursor.moveToNext()){
            int cardId = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_CARD));
            Boolean correct = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_CORRECT)) == 1;
            if(correct){
                correctCards.add(ExternalCardManager.INSTANCE.getCard(cardId));
            }
            else{
                incorrectCards.add(ExternalCardManager.INSTANCE.getCard(cardId));
            }
            int startTime = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_ASKED_AT));
            int endTime = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_ANSWERED_AT));
            times.add(Long.valueOf(endTime - startTime));
        }
        long sum = 0;
        for(Long l : times){
            sum += l;
        }
        Long avgTime = Long.valueOf(sum / times.size());
        return new ExternalStatistics(correctCards, incorrectCards, avgTime);
    }

    @Override
    public Statistics forCard(Card c) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.buildSelectQuery(
                AnswerEntry.TABLE_NAME,
                Arrays.asList(AnswerEntry.COLUMN_CARD + "=" + ((ExternalCard) c).getId())
        );
        Cursor cursor = db.rawQuery(query, null);
        List<Card> correctCards = new ArrayList<Card>();
        List<Card> incorrectCards = new ArrayList<Card>();
        List<Long> times = new ArrayList<Long>();
        while(cursor.moveToNext()){
            Boolean correct = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_CORRECT)) == 1;
            if(correct){
                correctCards.add(c);
            }
            else {
                incorrectCards.add(c);
            }
            int startTime = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_ASKED_AT));
            int endTime = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_ANSWERED_AT));
            times.add(Long.valueOf(endTime - startTime));
        }
        long sum = 0;
        for(Long l : times){
            sum += l;
        }
        Long avgTime = Long.valueOf(sum / times.size());
        return new ExternalStatistics(correctCards, incorrectCards, avgTime);
    }

    @Override
    public Statistics forDeck(Deck d) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.buildSelectQuery(
                AnswerEntry.TABLE_NAME,
                Arrays.asList(AnswerEntry.COLUMN_DECK + "=" + ((ExternalDeck) d).getDeckId())
        );
        Cursor cursor = db.rawQuery(query, null);
        List<Card> correctCards = new ArrayList<Card>();
        List<Card> incorrectCards = new ArrayList<Card>();
        List<Long> times = new ArrayList<Long>();
        while(cursor.moveToNext()){
            int cardId = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_CARD));
            Boolean correct = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_CORRECT)) == 1;
            if(correct){
                correctCards.add(ExternalCardManager.INSTANCE.getCard(cardId));
            }
            else{
                incorrectCards.add(ExternalCardManager.INSTANCE.getCard(cardId));
            }
            int start = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_ASKED_AT));
            int end = cursor.getInt(cursor.getColumnIndex(AnswerEntry.COLUMN_ANSWERED_AT));
            times.add(Long.valueOf(end - start));
        }
        long sum = 0;
        for(Long l : times){
            sum += l;
        }
        Long avgTime = Long.valueOf(sum / times.size());
        return new ExternalStatistics(correctCards, incorrectCards, avgTime);
    }
}
