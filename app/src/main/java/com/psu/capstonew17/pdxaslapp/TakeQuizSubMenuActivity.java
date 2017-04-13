package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.psu.capstonew17.pdxaslapp.BaseActivity;
import com.psu.capstonew17.pdxaslapp.FlashCardActivity;
import com.psu.capstonew17.pdxaslapp.MultipleChoiceActivity;
import com.psu.capstonew17.pdxaslapp.R;
import com.psu.capstonew17.pdxaslapp.WriteUpActivity;

public class TakeQuizSubMenuActivity extends BaseActivity implements View.OnClickListener {
    private Button bttMultipleChoice, bttFlashCard, bttWRiteUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz_submenu);

        bttMultipleChoice = (Button) this.findViewById(R.id.button_multiple_choice);
        bttMultipleChoice.setOnClickListener(this);

        bttFlashCard = (Button) this.findViewById(R.id.button_flash_card);
        bttFlashCard.setOnClickListener(this);

        bttWRiteUp = (Button) this.findViewById(R.id.button_write_up);
        bttWRiteUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {

            case R.id.button_multiple_choice:
                intent = new Intent(getApplicationContext(), MultipleChoiceActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_flash_card:
                intent = new Intent(getApplicationContext(), FlashCardActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_write_up:
                intent = new Intent(getApplicationContext(), WriteUpActivity.class);
                startActivity(intent);
                finish();
                break;

        }

    }
}
