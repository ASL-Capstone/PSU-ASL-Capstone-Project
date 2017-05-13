//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

public class TakeQuizSubMenuActivity extends BaseActivity {
    private int numQuestions;
    private enum  quizType{NONE,FLASH_CARD,MULTIPLE_CHOICE,WRITE_UP}
    private quizType quizOption;
    private RadioGroup numGroup;
    private TextView numPrompt;
    private LinearLayout deckLayout;
    private Space space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        numQuestions = 0;
        quizOption = quizType.NONE;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz_submenu);
        deckLayout = (LinearLayout)findViewById(R.id.deckListLayout);


        //get Decks
        //TODO uncomment back end call in quiz selector activity when ready
        //get Decks from backend
        //ArrayList<Deck> decksList = new ArrayList<>(ExternalDeckManager.getInstance(this).getDecks(null));

        //get Decks from current testing
        ArrayList<Deck> deckList = new ArrayList<>(TestingStubs.manyDecks());

        //set the multiple choice specific part to be invisible
        numGroup = (RadioGroup) findViewById(R.id.radioGroupQuestionCount);
        numGroup.setVisibility(View.GONE);
        numPrompt = (TextView) findViewById(R.id.textViewChooseNumberOfQuestions);
        numPrompt.setVisibility(View.GONE);



        int numDecks = deckList.size();
        for(int i = 0; i < numDecks; ++i){
            final String deckName = (deckList.get(i).getName());
            CheckBox ch = new CheckBox(this);
            ch.setText(deckName);
            deckLayout.addView(ch);

            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    View row = (View) buttonView.getParent();
                    if (isChecked) {
                        Log.d("item", "Item " + deckName + " is " + isChecked);
                    } else {
                        Log.d("item", "Item " + deckName + " is " + isChecked);
                    }
                }
            });
        }
    }


    public void takeQuizButtonClicked(View view){
        Intent intent;
        ArrayList<String> decks = new ArrayList<String>();
        if(quizOption == quizType.MULTIPLE_CHOICE && numQuestions == 0) {
            //Error tell user to make selection
            Toast.makeText(this, "ERROR Enter number of Questions", Toast.LENGTH_SHORT).show();
        }
        else {
            //Bundle the Deck Names to be sent to the quiz.
            int checks = deckLayout.getChildCount();
            for (int i = 0; i < checks; ++i) {
                CheckBox cb = (CheckBox) deckLayout.getChildAt(i);
                if (cb.isChecked()) {
                    decks.add(cb.getText().toString());
                }
            }
            // Check for No Checks
            if (decks.size() == 0) {
                Toast.makeText(this, "ERROR Select a Deck", Toast.LENGTH_SHORT).show();
            }
            else {
                switch (quizOption) {
                    case MULTIPLE_CHOICE:
                        // setup next intent
                        intent = new Intent(this, MultipleChoiceActivity.class);
                        Bundle questionCount = new Bundle();
                        questionCount.putInt("numQuestions", numQuestions);
                        intent.putExtras(questionCount);
                        intent.putStringArrayListExtra("Decks",decks);
                        startActivity(intent);
                        finish();
                        break;
                    case FLASH_CARD:
                        intent = new Intent(this, FlashCardActivity.class);
                        intent.putStringArrayListExtra("Decks",decks);
                        startActivity(intent);
                        finish();
                        break;
                    case WRITE_UP:
                        intent = new Intent(this, WriteUpActivity.class);
                        intent.putStringArrayListExtra("Decks",decks);
                        startActivity(intent);
                        finish();
                        break;
                }
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
