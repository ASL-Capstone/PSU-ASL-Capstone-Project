//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.pdxaslapp;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

public class CreateEditDeleteDeckActivity extends BaseActivity{
    static final int        MIN_CARDS       = 2;
    static final int        MAX_STRG_LNGTH  = 50;
    static final String     CHECKED_DECK   = "checkedDeck";

    private RadioGroup  deckRG;
    private TextView    noDeckMsg;
    private List<Deck>  decks;
    private DeckManager deckManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_delete_deck);

        noDeckMsg = (TextView) findViewById(R.id.noDecksText);
        noDeckMsg.setText(R.string.no_decks_msg);

        deckManager = ExternalDeckManager.getInstance(this);
        deckRG      = (RadioGroup) findViewById(R.id.deckRButtons);
        populateRadioGroup();
    }

    //populates the radiogroup with decks from the DB, unchecks any selected deck.
    public void populateRadioGroup(){
        deckRG.clearCheck();
        deckRG.removeAllViews();
        decks = deckManager.getDecks(null);

        //if there aren't any decks then display a message so that the user doesn't think
        //that this menu is broken
        if(decks.size() > 0)
            noDeckMsg.setVisibility(View.GONE);
        else
            noDeckMsg.setVisibility(View.VISIBLE);

        for (int i = 0; i < decks.size(); i++) {
            RadioButton currRad = new RadioButton(this);
            currRad.setId(i);
            currRad.setText(decks.get(i).getName());
            deckRG.addView(currRad);
        }
    }

    //the user wants to create a new deck
    public void onCreateClicked(View view) {
        Intent intent;
        intent = new Intent(this, CreateDeckActivity.class);
        startActivity(intent);
    }

    //the user wants to delete an existing deck
    public void onDeleteClicked(View view) {
        int index = deckRG.getCheckedRadioButtonId();
        //if the index is -1 then the user hasn't selected any decks. silly user.
        if(index == -1) {
            Toast.makeText(this, R.string.deck_not_selected, Toast.LENGTH_SHORT).show();

        //delete the selected deck, repopulate the radio group so that it's accurate.
        } else {
            Deck selectedDeck = decks.get(index);
            selectedDeck.delete();
            selectedDeck.commit();
            populateRadioGroup();
        }
    }

    //the user wants to edit an existing deck.
    public void onEditClicked(View view) {
        int index = deckRG.getCheckedRadioButtonId();
        //if the index is -1 then the user hasn't selected a deck. silly user
        if(index == -1) {
            Toast.makeText(this, R.string.deck_not_selected, Toast.LENGTH_SHORT).show();

        } else {
            Intent intent;
            intent = new Intent(this, EditDeckActivity.class);
            intent.putExtra(CHECKED_DECK, decks.get(index).getName());
            startActivity(intent);
        }
    }

    @Override
    //we need to repopulate the radio group after returning from creating or editing a deck
    protected void onResume(){
        super.onResume();
        populateRadioGroup();
    }
}
