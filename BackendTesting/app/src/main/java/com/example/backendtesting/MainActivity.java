package com.example.backendtesting;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.backendtesting.backend.api.*;
import com.example.backendtesting.backend.db.AslDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AslDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new AslDbHelper(getApplicationContext());
        dbHelper.dropTables(dbHelper.getWritableDatabase());
        dbHelper.createTables(dbHelper.getWritableDatabase());
        Context context = getApplicationContext();
        Log.println(Log.INFO, "db path", dbHelper.getReadableDatabase().getPath());

        VideoManager videoManager = new VideoManager(context);
        Video v1 = videoManager.saveVideo("pathtovideo1");
        Video v2 = videoManager.saveVideo("pathtovideo2");
        Video v3 = videoManager.saveVideo("pathtovideo1");

        DeckManager deckManager = new DeckManager(context);
        Deck defaultDeck = deckManager.createDeck("default");
        Deck d1 = deckManager.createDeck("animals");
        Card c1 = deckManager.createCard(v1, "dog", d1);
        Card c2 = deckManager.createCard(v2, "cat", d1);
        Card c3 = deckManager.createCard(v3, "running");

        TestManager testManager = new TestManager(context);
        testManager.recordAnswer(new Date(), c1, d1, Answer.QuestionType.MULTIPLE_CHOICE, true);
        testManager.recordAnswer(new Date(), c2, d1, Answer.QuestionType.MULTIPLE_CHOICE, true);
        testManager.recordAnswer(new Date(), c3, defaultDeck, Answer.QuestionType.PHRASE, false);


        deckManager.deleteCard(c1);
    }

}
