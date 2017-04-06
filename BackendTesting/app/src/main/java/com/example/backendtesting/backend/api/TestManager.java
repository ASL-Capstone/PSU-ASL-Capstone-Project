package com.example.backendtesting.backend.api;


import android.content.Context;
import java.util.Date;

import com.example.backendtesting.backend.data.Answer;
import com.example.backendtesting.backend.db.AslDbHelper;


public class TestManager {
    private AslDbHelper dbHelper;

    public TestManager(Context context){
        dbHelper = new AslDbHelper(context);
    }

    public void recordAnswer(Date date, Card card, Deck deck, Answer.QuestionType type, boolean correct){
        dbHelper.insertAnswer(date, card, deck, type, correct);
    }
}
