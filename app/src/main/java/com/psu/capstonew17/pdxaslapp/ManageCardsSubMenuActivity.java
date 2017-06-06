//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageCardsSubMenuActivity extends BaseActivity implements View.OnClickListener{
    private Button bttCreateCard, bttDeleteCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cards_sub_menu);

        // declare layout components
        bttCreateCard = (Button) this.findViewById(R.id.button_create_card);
        bttDeleteCard = (Button) this.findViewById(R.id.button_delete_card);

        // enable onClickListener on decalered buttons
        bttCreateCard.setOnClickListener(this);
        bttDeleteCard.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {

            case R.id.button_create_card:
                intent = new Intent(getApplicationContext(), CreateCardActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_delete_card:
                intent = new Intent(getApplicationContext(), DeleteCardActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }
}
