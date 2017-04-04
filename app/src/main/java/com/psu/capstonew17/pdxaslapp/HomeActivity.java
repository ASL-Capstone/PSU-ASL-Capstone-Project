package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private Button bttTakeQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        bttTakeQuiz = (Button) this.findViewById(R.id.button_take_quiz);
        bttTakeQuiz.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_take_quiz:
                Intent intent = new Intent(getApplicationContext(), TakeQuizActivity.class);
                startActivity(intent);
                break;

            case R.id.button_manage_decks:
                finish();
                break;

            case R.id.button_manage_cards:
                finish();
                break;
        }
    }
}
