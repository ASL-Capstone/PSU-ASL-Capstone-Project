//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaController;
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
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalTestManager;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.testMultiChoiceTest;

import java.util.ArrayList;
import java.util.List;

public class FlashCardActivity extends BaseActivity implements View.OnClickListener {
    private Button bttShow;
    private Button bttNext;
    // Names Passed into the activity from quiz selection activity.
    ArrayList<String> deckNamesForQuiz;
    // Used for test generation when the back end is hooked in.
    ArrayList<Deck> decksForQuiz;
    // The Test that is being used for this quiz
    Test currTest;
    // The Radio Group that is dynamically filled with the potential answers for the question.
    RadioGroup answers;
    // The Question that is being currently presented to the User.
    Question curQuestion;
    private VideoView vidDisplay;
    private TextView answerDisplay;
    private android.widget.MediaController mediaController;

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
        mediaController = new android.widget.MediaController(this);
        // Unpack the bundles list of Deck Names and the number of Questions for the Quiz
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deckNamesForQuiz = new ArrayList<>(extras.getStringArrayList("Decks"));
        }
        // Shouldn't happen but is here to help with dev.
        if(deckNamesForQuiz == null) {
            Toast.makeText(this, "Error! No Decks Selected" , Toast.LENGTH_SHORT).show();
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
        opts.questionTypes = TestManager.Options.QUESTION_TEXT_ENTRY;
        currTest = ExternalTestManager.getInstance(this).buildTest((List<Deck>)decksForQuiz,opts);
        // currTest = new testMultiChoiceTest();
        loadQuestion();
    }

    protected void loadQuestion(){
        // Check to See if there is another Question in the Test
        vidDisplay.setVisibility(View.INVISIBLE);
        if(currTest.hasNext()) {
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
                curQuestion.getVideo().configurePlayer(vidDisplay);
                vidDisplay.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Toast.makeText(getBaseContext(), "Error Playing Video", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                vidDisplay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        vidDisplay.setMediaController(mediaController);
                        mp.start();
                    }
                });
                vidDisplay.setVisibility(View.VISIBLE);
                break;
        }

    }
}
