//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageDecksSubMenuActivity extends BaseActivity implements View.OnClickListener {
    private Button bttcedDecks, bttReceiveDeck, bttShareDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_decks_sub_menu);

        // declare layout components
        bttcedDecks = (Button) this.findViewById(R.id.button_ced_decks);
        bttReceiveDeck = (Button) this.findViewById(R.id.button_receive_deck);
        bttShareDeck = (Button) this.findViewById(R.id.button_share_deck);

        // enable onClickListener on decalered buttons
        bttcedDecks.setOnClickListener(this);
        bttReceiveDeck.setOnClickListener(this);
        bttShareDeck.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {

            case R.id.button_ced_decks:
                intent = new Intent(getApplicationContext(), CreateEditDeleteDeckActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_receive_deck:
                intent = new Intent(getApplicationContext(), ReceiveDeckActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_share_deck:
                intent = new Intent(getApplicationContext(), ShareDeckActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
