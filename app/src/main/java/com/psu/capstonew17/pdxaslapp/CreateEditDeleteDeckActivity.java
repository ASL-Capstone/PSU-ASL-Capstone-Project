//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.pdxaslapp;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

public class CreateEditDeleteDeckActivity extends BaseActivity implements View.OnClickListener{
    static final int        MIN_CARDS       = 2;
    static final int        MAX_STRG_LNGTH  = 50;
    static final String     CHECKED_DECK   = "checkedDeck";

    private RadioGroup  deckRG;
    private List<Deck>  decks;
    private DeckManager deckManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_delete_deck);

        findViewById(R.id.bttnCrt).setOnClickListener(this);
        findViewById(R.id.bttnDlt).setOnClickListener(this);
        findViewById(R.id.bttnEdit).setOnClickListener(this);

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
            findViewById(R.id.noDecksText).setVisibility(View.GONE);
        else
            findViewById(R.id.noDecksText).setVisibility(View.VISIBLE);

        for (int i = 0; i < decks.size(); i++) {
            RadioButton currRad = new RadioButton(this);
            currRad.setId(i);
            currRad.setText(decks.get(i).getName());
            deckRG.addView(currRad);
        }
    }

    @Override
    public void onClick(View view) {
        int index = deckRG.getCheckedRadioButtonId();
        Intent intent;
        switch(view.getId()) {
            //user wants to record a video for the new card
            case R.id.bttnCrt:
                intent = new Intent(this, CreateEditDeckActivity.class);
                startActivity(intent);
                break;

            //the user wants to delete an existing deck
            case R.id.bttnDlt:
                //if the index is -1 then the user hasn't selected any decks. silly user.
                if (index == -1) {
                    Toast.makeText(this, R.string.deck_not_selected, Toast.LENGTH_SHORT).show();

                    //delete the selected deck, repopulate the radio group so that it's accurate.
                } else {

                    deleteDeck(index);
                }
                break;

            //the user wants to edit an existing deck.
            case R.id.bttnEdit:
                //if the index is -1 then the user hasn't selected a deck. silly user
                if (index == -1) {
                    Toast.makeText(this, R.string.deck_not_selected, Toast.LENGTH_SHORT).show();

                } else {
                    intent = new Intent(this, CreateEditDeckActivity.class);
                    intent.putExtra(CHECKED_DECK, decks.get(index).getName());
                    startActivity(intent);
                }
                break;
        }
    }

    // prompt confirmation with user and delete deck
    public void deleteDeck(final int index) {
        //building the message to be displayed
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.prompt_deleted_deck));

        //create the alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false);
        builder.setMessage(sb.toString());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            //if they want continue then go ahead and delte the decks and the card
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // delete deck
                Deck selectedDeck = decks.get(index);
                selectedDeck.delete();
                selectedDeck.commit();
                populateRadioGroup();

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            //if not then don't do anything
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //show the alert dialog
        builder.create().show();
    }


    @Override
    //we need to repopulate the radio group after returning from creating or editing a deck
    protected void onResume(){
        super.onResume();
        populateRadioGroup();
    }
}
