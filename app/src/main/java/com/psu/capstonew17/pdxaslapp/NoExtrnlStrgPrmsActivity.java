//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

//activity that's shown when the user refuses to give the app external storage perms.
public class NoExtrnlStrgPrmsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_extrnl_strg_prms);
        Button bttnOk = (Button) this.findViewById(R.id.button_ok);
        bttnOk.setOnClickListener(this);

    }

    //send the user to the android home screen after clicking ok
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_ok:
                finish();
                break;
        }
    }
}
