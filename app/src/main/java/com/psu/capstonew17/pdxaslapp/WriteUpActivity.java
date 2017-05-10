//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class WriteUpActivity extends BaseActivity implements View.OnClickListener {
    // Accuracy Vars
    private int correct;
    private int total;
    // Submit Answer Button
    private Button bttSubmit;
    // Names Passed into the activity from quiz selection activity.
    private ArrayList<String> deckNamesForQuiz;
    // Used for test generation when the back end is hooked in.
    private ArrayList<Deck> decksForQuiz;
    // The Test that is being used for this quiz
    private Test currTest;
    // TODO Hook up actual Test Manager
    private TestManager testManager;
    // The Question that is being currently presented to the User.
    Question curQuestion;
    private VideoView vidDisplay;
    private EditText answerInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_write_up);
        // Declare and enable buttons
        bttSubmit = (Button) this.findViewById(R.id.button_quizWriteUpSubmit);
        bttSubmit.setOnClickListener(this);
        // Setup VideoView and Text Display
        answerInput = (EditText) findViewById(R.id.editTextWriteUpAnswerField);
        vidDisplay = (VideoView) findViewById(R.id.videoViewWriteUpQuiz);
        // Unpack the bundles list of Deck Names and the number of Questions for the Quiz
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deckNamesForQuiz = new ArrayList<>(extras.getStringArrayList("Decks"));
        }
        // Shouldn't happen but is here to help with dev.
        else if (deckNamesForQuiz == null) {
            Toast.makeText(this, "Error! No Decks Selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Testing params passed in
        // TODO Remove after testing
        for (int i = 0; i < deckNamesForQuiz.size(); ++i) {
            Toast.makeText(this, "Selected Deck " + deckNamesForQuiz.get(i), Toast.LENGTH_SHORT).show();
        }
        // Get the generic Test
        // TODO Update to using the actual backend quiz generation
        currTest = new testMultiChoiceTest();
        loadQuestion();

    }

    protected void loadQuestion() {
        // Check to See if there is another Question in the Test
        if (currTest.hasNext()) {
            // TODO hook video up
            Uri video = Uri.parse("android.resource://com.psu.capstonew17.pdxaslapp/raw/brook_vid1");
            vidDisplay.setVideoURI(video);
            vidDisplay.start();
            curQuestion = currTest.next();
            answerInput.getText().clear();
        }
        //No more questions leave quiz activity
        else {
            Intent intent = new Intent(this, CompleteQuizActivity.class);
            intent.putExtra("NumCorrect", correct);
            intent.putExtra("totalNum", total);
            startActivity(intent);
            finish();
        }
    }


    protected void processQuestion() {
        String input = answerInput.getText().toString();
        Pair<Boolean, String> answerReturn = curQuestion.answer(input);
        String correctAnswer = answerReturn.second;
        if (answerReturn.first) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            correct += 1;
        } else {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }
        total += 1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_quizWriteUpSubmit:
                processQuestion();
                loadQuestion();
                break;
            case R.id.editTextWriteUpAnswerField:
                answerInput.setText(" ");
                break;
        }
    }

}
