package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by thanhhoang on 4/4/17.
 */

public class FlashCardActivity extends BaseActivity implements View.OnClickListener {
    private Button bttSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
