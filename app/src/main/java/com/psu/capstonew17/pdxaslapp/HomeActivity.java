//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private Button bttTakeQuiz;
    private Button bttManageCards;
    private Button bttManageDecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

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
