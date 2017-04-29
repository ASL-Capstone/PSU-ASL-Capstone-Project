package com.psu.capstonew17.pdxaslapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CompleteQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_quiz);

        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(R.string.quiz_complete);
    }
}
