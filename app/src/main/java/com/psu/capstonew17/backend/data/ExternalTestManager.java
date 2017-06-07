//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.db.AslDbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExternalTestManager implements TestManager{
    static ExternalTestManager INSTANCE = new ExternalTestManager();

    private AslDbHelper dbHelper;

    public static TestManager getInstance(Context context){
        INSTANCE.dbHelper = AslDbHelper.getInstance(context);
        return INSTANCE;
    }

    AslDbHelper getDbHelper(){
        return dbHelper;
    }

    @Override
    public Test buildTest(List<Deck> sources, Options opts) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query;
        List<Question> questions = new ArrayList<Question>();
        for(Deck deck : sources) {
            query = dbHelper.buildSelectQuery(
                    RelationEntry.TABLE_NAME,
                    Arrays.asList(RelationEntry.COLUMN_DECK + "=" + deck.getDeckId())
            );
            Cursor cursor = db.rawQuery(query, null);
            List<Integer> cardIds = new ArrayList<Integer>();
            while(cursor.moveToNext()) {
                int cardId = cursor.getInt(cursor.getColumnIndex(RelationEntry.COLUMN_CARD));
                cardIds.add(cardId);
            }
            cursor.close();
            for(Integer id : cardIds){
                Card card = ExternalCardManager.INSTANCE.getCard(id);
                Question.Type t;
                if(opts.questionTypes == opts.QUESTION_MULTIPLE_CHOICE){
                    t = Question.Type.MULTIPLE_CHOICE;
                }
                else{
                    t = Question.Type.TEXT_ENTRY;
                }
                Question q = new ExternalQuestion(card, t, deck.getDeckId());
                questions.add(q);
            }
        }
        if(opts.mode.equals(OrderingMode.RANDOM)){
            Collections.shuffle(questions);
        }
        int numQuestions = questions.size();
        if(opts.count < questions.size()){
            numQuestions = opts.count;
        }
        Statistics stats = null;
        if(opts.recordStats){
            stats = new ExternalStatistics(new ArrayList<Card>(), new ArrayList<Card>(), 0l);
        }
        ExternalTest test = new ExternalTest(questions.subList(0, numQuestions), stats);
        for(Question q : test.getQuestions()){
            ((ExternalQuestion) q).addToTest(test);
        }
        return new ExternalTest(questions.subList(0, numQuestions), stats);
    }
}
