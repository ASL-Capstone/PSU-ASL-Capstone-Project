//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CompleteQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_quiz);

        TextView textView = (TextView) findViewById(R.id.completeQuizSplashText);
        textView.setText(R.string.quiz_complete);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            int correct = bundle.getInt("NumCorrect");
            int total = bundle.getInt("totalNum");
            TextView display = (TextView) findViewById(R.id.completeQuizCorrectText);
            display.setText("You got " + correct + " out of " + total + " questions!");
        }
    }
}
