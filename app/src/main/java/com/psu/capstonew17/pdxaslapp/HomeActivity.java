//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.psu.capstonew17.backend.data.*;

import android.support.v4.app.ActivityCompat;

public class HomeActivity extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private Button bttTakeQuiz;
    private Button bttManageCards;
    private Button bttManageDecks;
    final String SHARED_PREFERENCES_PATH = "asl_shared_preferences";
    final String FIRST_OPEN = "first_open";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCES_PATH, 0);

        // check if this is the first time opening the app
        if(settings.getBoolean(FIRST_OPEN, true)) {
            Log.println(Log.INFO, FIRST_OPEN, "installing base assets");
            BaseAssetsImporter.importAssets(this);
            // no longer first time
            settings.edit().putBoolean(FIRST_OPEN, false).commit();
        }

        // declare layout components
        bttTakeQuiz = (Button) this.findViewById(R.id.button_take_quiz);
        bttManageCards = (Button) this.findViewById(R.id.button_manage_cards);
        bttManageDecks = (Button) this.findViewById(R.id.button_manage_decks);

        // enable clickable on buttons
        bttTakeQuiz.setOnClickListener(this);
        bttManageCards.setOnClickListener(this);
        bttManageDecks.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_take_quiz:
                intent = new Intent(getApplicationContext(), TakeQuizSubMenuActivity.class);
                startActivity(intent);
                break;

            case R.id.button_manage_decks:
                intent = new Intent(getApplicationContext(), ManageDecksSubMenuActivity.class);
                startActivity(intent);
                break;

            case R.id.button_manage_cards:
                intent = new Intent(getApplicationContext(), ManageCardsSubMenuActivity.class);
                startActivity(intent);
                break;
        }
    }
}
