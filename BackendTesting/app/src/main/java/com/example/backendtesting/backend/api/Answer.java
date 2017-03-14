package com.example.backendtesting.backend.api;

import android.content.Context;

import com.example.backendtesting.backend.db.AslDbHelper;

import java.util.Date;

public class Answer {
    public int answerId;
    public Date createdAt;
    public int cardId;
    public int deckId;
    public QuestionType type;
    public boolean correct;

    public enum QuestionType{
        MULTIPLE_CHOICE, PHRASE
    }

    public Answer(int answerId, String createdAt, int cardId, int deckId, QuestionType type, boolean correct){
        this.answerId = answerId;
        this.cardId = cardId;
        this.deckId = deckId;
        this.type = type;
        this.correct = correct;
    }

    public static void recordAnswer(Context context){
        AslDbHelper dbHelper = new AslDbHelper(context);
        //dbHelper.insertAnswer();
    }
}
