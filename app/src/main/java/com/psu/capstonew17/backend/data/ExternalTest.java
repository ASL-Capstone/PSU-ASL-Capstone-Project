//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.db.AslDbHelper;

import java.util.Date;
import java.util.List;


class ExternalTest implements Test {
    private List<Question> questions;
    private int current;
    private Statistics statistics;

    private AslDbHelper dbHelper;

    public ExternalTest(List<Question> questions, Statistics statistics){
        this.questions = questions;
        this.current = 0;
        this.statistics = statistics;
    }

    public List<Question> getQuestions(){
        return this.questions;
    }

    @Override
    public Statistics getStats() {
        return statistics;
    }

    @Override
    public boolean hasNext() {
        return current < questions.size() && questions.get(current) != null;
    }

    @Override
    public Question next() {
        Question question = questions.get(current);
        dbHelper = ExternalTestManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnswerEntry.COLUMN_ASKED_AT, new Date().getTime());
        values.put(AnswerEntry.COLUMN_CARD, ((ExternalQuestion) question).getCardId());
        values.put(AnswerEntry.COLUMN_DECK, ((ExternalQuestion) question).getDeckId());
        values.put(AnswerEntry.COLUMN_TYPE, question.getType().ordinal());
        int questionId = (int) db.insert(AnswerEntry.TABLE_NAME, null, values);
        ((ExternalQuestion) question).setQuestionId(questionId);
        current += 1;
        return question;
    }
}
