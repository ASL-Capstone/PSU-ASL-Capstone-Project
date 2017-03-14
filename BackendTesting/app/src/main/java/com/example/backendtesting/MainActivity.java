package com.example.backendtesting;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.backendtesting.backend.api.*;
import com.example.backendtesting.backend.db.AslDbHelper;

public class MainActivity extends AppCompatActivity {

    private AslDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new AslDbHelper(getApplicationContext());
        Log.println(Log.INFO, "db path", dbHelper.getReadableDatabase().getPath());

        dbHelper.dropTables(dbHelper.getWritableDatabase());
        dbHelper.createTables(dbHelper.getWritableDatabase());
        Context context = getApplicationContext();

        // test saving some stuff
        Video v1 = Video.saveVideo("somepath", getApplicationContext());
        Video v2 = Video.saveVideo("someOTHERpath", getApplicationContext());
        Log.println(Log.INFO, "test_video_insert", "id: " + v1.videoId + ", path: " + v1.videoPath + ", sha: " + v1.videoSha);
        Log.println(Log.INFO, "test_video_insert", "id: " + v2.videoId + ", path: " + v2.videoPath + ", sha: " + v2.videoSha);

        Card c1 = Card.createCard(v1, "airplane", getApplicationContext());
        Card c2 = Card.createCard(v2, "boat", getApplicationContext());
        Log.println(Log.INFO, "test_card_insert", "id: " + c1.cardId + ", vid: " + c1.videoId + ", answer: " + c1.answer);
        Log.println(Log.INFO, "test_card_insert", "id: " + c2.cardId + ", vid: " + c2.videoId + ", answer: " + c2.answer);

        Deck defaultDeck = Deck.createDeck("default", getApplicationContext());
        Log.println(Log.INFO, "test_create_deck", "id: " + defaultDeck.deckId + ", name: " + defaultDeck.deckName);
        defaultDeck.addCardToDeck(c1, getApplicationContext());
        defaultDeck.addCardToDeck(c2, getApplicationContext());

    }

}
