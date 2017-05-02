//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FlashCardActivity extends BaseActivity implements View.OnClickListener {
    private Button bttSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_flash_card);

        // declare and enable clickable on button
        bttSubmit = (Button) this.findViewById(R.id.button_submit);
        bttSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO: add handling on click on submit button
    }
}
