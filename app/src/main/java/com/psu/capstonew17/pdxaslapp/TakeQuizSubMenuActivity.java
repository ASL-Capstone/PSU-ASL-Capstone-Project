package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.psu.capstonew17.pdxaslapp.BaseActivity;
import com.psu.capstonew17.pdxaslapp.FlashCardActivity;
import com.psu.capstonew17.pdxaslapp.MultipleChoiceActivity;
import com.psu.capstonew17.pdxaslapp.R;
import com.psu.capstonew17.pdxaslapp.WriteUpActivity;

public class TakeQuizSubMenuActivity extends BaseActivity {
    private int numQuestions;
    private enum  quizType{NONE,FLASH_CARD,MULTIPLE_CHOICE,WRITE_UP}
    private quizType quizOption;
    private RadioGroup numGroup;
    private TextView numPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        numQuestions = 0;
        quizOption = quizType.NONE;

        //TODO populate list view of decks
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz_submenu);
        numGroup = (RadioGroup) findViewById(R.id.radioGroupQuestionCount);
        numGroup.setVisibility(View.GONE);
        numPrompt = (TextView) findViewById(R.id.textViewChooseNumberOfQuestions);
        numPrompt.setVisibility(View.GONE);
    }


    public void takeQuizButtonClicked(View view){
        Intent intent;
        if(quizOption == quizType.MULTIPLE_CHOICE && numQuestions == 0){
            //Error tell user to make selection
            Toast.makeText(this, "ERROR Enter number of Questions", Toast.LENGTH_SHORT).show();
        }
        else {
            switch (quizOption) {
                case MULTIPLE_CHOICE:
                    intent = new Intent(this, MultipleChoiceActivity.class);
                    Bundle questionCount = new Bundle();
                    questionCount.putInt("numQuestions", numQuestions);
                    intent.putExtras(questionCount);
                    startActivity(intent);
                    break;
                case FLASH_CARD:
                    intent = new Intent(this, FlashCardActivity.class);
                    startActivity(intent);
                    break;
                case WRITE_UP:
                    intent = new Intent(this, WriteUpActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    public void onQuizTypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButtonFlashCard:
                if(checked) {
                    quizOption = quizType.FLASH_CARD;
                    numGroup.setVisibility(View.GONE);
                    numPrompt.setVisibility(View.GONE);
                }
                break;
            case R.id.radioButtonMultipleChoice:
                if(checked) {
                    quizOption = quizType.MULTIPLE_CHOICE;
                    numGroup.setVisibility(View.VISIBLE);
                    numPrompt.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radioButtonWriteUp:
                if(checked) {
                    quizOption = quizType.WRITE_UP;
                    numGroup.setVisibility(View.GONE);
                    numPrompt.setVisibility(View.GONE);
                }
                break;
        }

    }

    public void onNumQuestionsClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButtonFiftyQuestions:
                if(checked)
                    numQuestions = 50;
                break;
            case R.id.radioButtonThirtyQuestions:
                if(checked)
                    numQuestions = 30;
                break;
            case R.id.radioButtonTenQuestions:
                if(checked)
                    numQuestions = 10;
                break;
        }

    }
}
