//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Test;
import com.psu.capstonew17.backend.api.TestManager;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.testMultiChoiceTest;

import java.util.ArrayList;

public class FlashCardActivity extends BaseActivity implements View.OnClickListener {
    private Button bttShow;
    private Button bttNext;
    // Names Passed into the activity from quiz selection activity.
    ArrayList<String> deckNamesForQuiz;
    // Used for test generation when the back end is hooked in.
    ArrayList<Deck> decksForQuiz;
    // The Test that is being used for this quiz
    Test currTest;
    // TODO Hook up actual Test Manager
    TestManager testManager;
    // The Radio Group that is dynamically filled with the potential answers for the question.
    RadioGroup answers;
    // The Question that is being currently presented to the User.
    Question curQuestion;
    private VideoView vidDisplay;
    private TextView answerDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Basic Setup for the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_flash_card);
        // Declare and enable buttons
        bttShow = (Button) this.findViewById(R.id.button_showAnswer);
        bttShow.setOnClickListener(this);
        bttNext = (Button) this.findViewById(R.id.button_nextFlashCard);
        bttNext.setOnClickListener(this);
        // Setup VideoView and Text Display
        answerDisplay = (TextView) findViewById(R.id.textView_FlashCardAnswer);
        vidDisplay = (VideoView) findViewById(R.id.videoView_flashCard);
        // Unpack the bundles list of Deck Names and the number of Questions for the Quiz
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deckNamesForQuiz = new ArrayList<>(extras.getStringArrayList("Decks"));
        }
        // Shouldn't happen but is here to help with dev.
        else if(deckNamesForQuiz == null) {
            Toast.makeText(this, "Error! No Decks Selected" , Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Testing params passed in
        // TODO Remove after testing
        for (int i = 0; i < deckNamesForQuiz.size(); ++i){
            Toast.makeText(this, "Selected Deck " + deckNamesForQuiz.get(i), Toast.LENGTH_SHORT).show();
        }
        // Get the generic Test
        // TODO Update to using the actual backend quiz generation
        currTest = new testMultiChoiceTest();
        loadQuestion();
    }

    protected void loadQuestion(){
        // Check to See if there is another Question in the Test
        if(currTest.hasNext()) {
            // TODO hook video up
            curQuestion = currTest.next();
            Pair<Boolean,String> answerReturn = curQuestion.answer(" ");
            String correctAnswer = answerReturn.second;
            answerDisplay.setText(correctAnswer);
        }
        //No more questions leave quiz activity
        else{
            Intent intent = new Intent(this,CompleteQuizActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_nextFlashCard:
                loadQuestion();
                break;
            case R.id.button_showAnswer:
                //TODO start video display
                break;
        }

    }
}
