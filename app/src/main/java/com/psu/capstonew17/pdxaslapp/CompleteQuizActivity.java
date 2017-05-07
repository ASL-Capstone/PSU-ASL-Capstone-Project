//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CompleteQuizActivity extends AppCompatActivity implements View.OnClickListener{
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_quiz);

        TextView textView = (TextView) findViewById(R.id.completeQuizSplashText);
        textView.setText(R.string.quiz_complete);
        TextView display = (TextView) findViewById(R.id.completeQuizCorrectText);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            int correct = bundle.getInt("NumCorrect");
            int total = bundle.getInt("totalNum");
            display.setText("You got " + correct + " out of " + total + " questions!");
        }
        else{
            display.setText(" ");
        }
        homeButton = (Button) findViewById(R.id.button_home);
        homeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
