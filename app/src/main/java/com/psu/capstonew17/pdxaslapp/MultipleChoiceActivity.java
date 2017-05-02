//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Test;
import com.psu.capstonew17.backend.api.TestManager;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.testMultiChoiceTest;

import java.util.ArrayList;

public class MultipleChoiceActivity extends BaseActivity implements View.OnClickListener {
    ArrayList<String> deckNamesForQuiz;
    ArrayList<Deck> decksForQuiz;
    int numQuestions;
    Test currTest;
    Button submit;
    //TODO Hook up actual Test Manager
    TestManager testManager;
    RadioGroup answers;
    Question curQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_multiple_choice);
        deckNamesForQuiz = new ArrayList<>(getIntent().getExtras().getStringArrayList("Decks"));
        numQuestions = getIntent().getExtras().getInt("numQuestions");
        //Testing params passed in
        //TODO Remove after testing
        Toast.makeText(this, "Number of Questions: " + numQuestions, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < deckNamesForQuiz.size(); ++i){
            Toast.makeText(this, "Selected Deck " + deckNamesForQuiz.get(i), Toast.LENGTH_SHORT).show();
        }
        //get the generic Test
        currTest = new testMultiChoiceTest();
        answers = (RadioGroup)findViewById(R.id.MultiChoiceAnswerRadioGroup);
        submit = (Button)findViewById(R.id.button_submit);
        submit.setOnClickListener(this);
        loadQuestion();
    }

    //currently crashes after first answer: will fix in the morning
    protected void loadQuestion(){
        if(currTest.hasNext()) {
            answers.removeAllViews();
            curQuestion = currTest.next();
            for (String answer : curQuestion.getOptions()){
                RadioButton add = new RadioButton(this);
                add.setText(answer);
                answers.addView(add);
            }
            //TODO hook video up
        }
    }

    // Returns 1 if answer is correct and 0 if not, returns -1 if there is an error
    private int processAnswer(){
        int userAnswerID = answers.getCheckedRadioButtonId();
        if(curQuestion == null) {
            Toast.makeText(this, "Null Question!", Toast.LENGTH_SHORT).show();
            return -1;
        }
        if(userAnswerID != -1) {
            View holder = findViewById(userAnswerID);
            int radioId = answers.indexOfChild(holder);
            RadioButton selectedAnswer = (RadioButton) answers.getChildAt(radioId);
            Pair<Boolean,String> result = curQuestion.answer(selectedAnswer.getText().toString());
            if (result.first){
                return 1;
            }
            else {
                return 0;
            }
        }
        else {
            return -1;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                switch (processAnswer()) {
                    case 1:
                        Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(this, "Process Answer Error", Toast.LENGTH_SHORT).show();
                }
                loadQuestion();
                break;
            default:
                break;
        }
    }
}
