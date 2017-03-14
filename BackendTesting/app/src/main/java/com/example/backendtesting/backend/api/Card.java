package com.example.backendtesting.backend.api;

import android.content.Context;

import com.example.backendtesting.backend.db.AslDbHelper;

public class Card {
    public int cardId;
    public int videoId;
    public String answer;

    public Card(int id, int vId, String ans){
        cardId = id;
        videoId = vId;
        answer = ans;
    }

    public static Card createCard(Video video, String answer, Context context){
        AslDbHelper dbHelper = new AslDbHelper(context);
        return dbHelper.insertCard(video, answer);
    }

}

