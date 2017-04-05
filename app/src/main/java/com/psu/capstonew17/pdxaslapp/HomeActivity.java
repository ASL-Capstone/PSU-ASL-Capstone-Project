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

        // declare button and enable clickable on buttons
        bttTakeQuiz = (Button) this.findViewById(R.id.button_take_quiz);
        bttTakeQuiz.setOnClickListener(this);

        bttManageCards = (Button) this.findViewById(R.id.button_manage_cards);
        bttManageCards.setOnClickListener(this);

        bttManageDecks = (Button) this.findViewById(R.id.button_manage_decks);
        bttManageDecks.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_take_quiz:
                Intent intent = new Intent(getApplicationContext(), TakeQuizSubMenuActivity.class);
                startActivity(intent);
                break;

            case R.id.button_manage_decks:
                // TODO: add intent and start new activity after created activity for manage decks sub menu
                break;

            case R.id.button_manage_cards:
                // TODO: add intent and state new activity for manage cards sub menu
                break;
        }
    }
}
