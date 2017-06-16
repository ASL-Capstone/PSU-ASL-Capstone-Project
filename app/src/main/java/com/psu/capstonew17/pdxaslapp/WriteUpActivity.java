//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Test;
import com.psu.capstonew17.backend.api.TestManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalTestManager;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.testMultiChoiceTest;

import java.util.ArrayList;
import java.util.List;

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
    // The Question that is being currently presented to the User.
    Question curQuestion;
    private VideoView vidDisplay;
    private EditText answerInput;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_write_up);
        // Declare and enable buttons
        bttSubmit = (Button) this.findViewById(R.id.button_quizWriteUpSubmit);
        bttSubmit.setOnClickListener(this);
        mediaController = new MediaController(this);
        bttSubmit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    processQuestion();
                    loadQuestion();
                    return true;
                }
                return false;
            }
        });
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

        // Get the generic Test
        decksForQuiz = new ArrayList<>();
        for (String name : deckNamesForQuiz){
            Deck toAdd = ExternalDeckManager.getInstance(this).getDecks(name).get(0);
            if (toAdd != null)
                decksForQuiz.add(toAdd);
        }
        TestManager.Options opts = new TestManager.Options();
        opts.recordStats = false;
        opts.count = 999;
        opts.mode = TestManager.OrderingMode.RANDOM;
        opts.questionTypes = TestManager.Options.QUESTION_MULTIPLE_CHOICE;
        currTest = ExternalTestManager.getInstance(this).buildTest((List<Deck>)decksForQuiz,opts);
        //currTest = new testMultiChoiceTest();
        loadQuestion();

    }

    protected void loadQuestion() {
        // Check to See if there is another Question in the Test
        if (currTest.hasNext()) {
            curQuestion = currTest.next();
            answerInput.getText().clear();
            curQuestion.getVideo().configurePlayer(vidDisplay);
            vidDisplay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    vidDisplay.setMediaController(mediaController);
                    mp.start();
                }
            });
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
        String input = answerInput.getText().toString().trim();
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
                break;
        }
    }

}
