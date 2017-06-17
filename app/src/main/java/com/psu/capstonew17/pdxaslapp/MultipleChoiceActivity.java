//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Test;
import com.psu.capstonew17.backend.api.TestManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalTestManager;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceActivity extends BaseActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    // Names Passed into the activity from quiz selection activity.
    private ArrayList<String> deckNamesForQuiz;
    // Number of Question passed in from the quiz selection activity.
    private int numQuestions;
    // The Test that is being used for this quiz
    private Test currTest;
    // Submit button used by this activity
    private Button submit;
    private VideoView questionVideo;
    // Tracks number of questions the user has answered
    private int totalQuestions;
    // Tracks number of correct responses the user gave
    private int totalCorrect;
    // The Radio Group that is dynamically filled with the potential answers for the question.
    private RadioGroup answers;
    // The Question that is being currently presented to the User.
    private Question curQuestion;

    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Basic Setup for the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_multiple_choice);
        // Unpack the bundles list of Deck Names and the number of Questions for the Quiz
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deckNamesForQuiz = new ArrayList<>(extras.getStringArrayList("Decks"));
            numQuestions = extras.getInt("numQuestions");
        }
        // Shouldn't happen but is here to help with dev.
        else if(deckNamesForQuiz == null || numQuestions == 0) {
            Toast.makeText(this, "Error! No Decks Selected or Number of Questions is 0" , Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get the generic Test
        ArrayList<Deck> decksForQuiz;
        decksForQuiz = new ArrayList<>();
        for (String name : deckNamesForQuiz){
            try {
                Deck toAdd = ExternalDeckManager.getInstance(this).getDecks(name).get(0);
                if (toAdd != null)
                    decksForQuiz.add(toAdd);
            }
            catch (IndexOutOfBoundsException e){
                Toast.makeText(getBaseContext(), "Error Invalid Decks", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        TestManager.Options opts = new TestManager.Options();
        opts.recordStats = false;
        opts.count = numQuestions;
        opts.mode = TestManager.OrderingMode.RANDOM;
        opts.questionTypes = TestManager.Options.QUESTION_MULTIPLE_CHOICE;
        currTest = ExternalTestManager.getInstance(this).buildTest((List<Deck>)decksForQuiz,opts);
        // Testing stub call
        // currTest = new testMultiChoiceTest();
        // Hook up the Radio Container and the Submit Button
        answers = (RadioGroup)findViewById(R.id.MultiChoiceAnswerRadioGroup);
        submit = (Button)findViewById(R.id.button_mult_submit);
        submit.setOnClickListener(this);
        questionVideo = (VideoView) findViewById(R.id.videoViewMultiChoice);
        mediaController = new MediaController(this);

        // Initialize UI elements
        submit.setEnabled(false);
        answers.setOnCheckedChangeListener(this);

        // Load the First Question
        loadQuestion();
    }

    protected void loadQuestion(){
        // Check to See if there is another Question in the Test
        if(currTest.hasNext()) {
            // Clear the current set of answers from the Radio Group
            answers.clearCheck();
            answers.removeAllViews();
            // Get the Next Question
            curQuestion = currTest.next();
            // Get the different answers for the Question that was just loaded
            for (String answer : curQuestion.getOptions()){
                // Make a new button and set it's text to the answer provided by getOptions
                RadioButton add = new RadioButton(this);
                add.setText(answer);
                answers.addView(add);
            }
            // Test video
            curQuestion.getVideo().configurePlayer(questionVideo);
            questionVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getBaseContext(), "Error Playing Video", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            questionVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    questionVideo.setMediaController(mediaController);
                    mp.start();
                }
            });
        }
        //No more questions leave quiz activity
        else{
            Intent intent = new Intent(this,CompleteQuizActivity.class);
            intent.putExtra("NumCorrect",totalCorrect);
            intent.putExtra("totalNum",totalQuestions);
            startActivity(intent);
            finish();
        }
    }

    // Called by the submit button
    // Returns 1 if answer is correct and 0 if not, returns -1 if there is an error
    private int processAnswer(){
        // Get the checked Option from the Radio Group
        int userAnswerID = answers.getCheckedRadioButtonId();
        // SHOULDN'T HAPPEN! But is here to help debug
        if(curQuestion == null) {
            Toast.makeText(this, "Null Question!", Toast.LENGTH_SHORT).show();
            return -1;
        }
        // Make Sure that a button was checked
        if(userAnswerID != -1) {
            // Get the selected answer
            RadioButton holder = (RadioButton) findViewById(userAnswerID);

            // Check User answer against the current questions correct answer
            Pair<Boolean,String> result = curQuestion.answer(holder.getText().toString());
            // Case: Correct
            if (result.first){
                return 1;
            }
            // Case: Incorrect
            else {
                return 0;
            }
        }
        // If button was not checked return input error
        else {
            return -1;
        }
    }

    @Override
    public void onClick(View view) {
        // Currently only one needed button but added switch in case of future need
        switch (view.getId()) {
            // Case: User hit the submit button
            case R.id.button_mult_submit:
                switch (processAnswer()) {
                    // Case: User entered correct answer
                    case 1:
                        totalCorrect += 1;
                        totalQuestions += 1;
                        Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                        break;
                    // Case: User Entered incorrect answer
                    case 0:
                        totalQuestions += 1;
                        Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
                        break;
                    // Case: User did'nt enter an answer
                    case -1:
                        Toast.makeText(this, "Pick an Answer", Toast.LENGTH_SHORT).show();
                        return;
                }
                // Load the next question.
                loadQuestion();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        submit.setEnabled(radioGroup.getCheckedRadioButtonId() != -1);
    }
}
