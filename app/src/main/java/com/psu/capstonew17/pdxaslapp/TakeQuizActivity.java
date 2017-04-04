package com.psu.capstonew17.pdxaslapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TakeQuizActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            default:
                finish();
                break;
        }

    }
}
