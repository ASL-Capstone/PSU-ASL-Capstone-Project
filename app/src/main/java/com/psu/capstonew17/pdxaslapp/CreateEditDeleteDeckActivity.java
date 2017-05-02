//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.pdxaslapp;

import java.util.List;
import java.util.ListIterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

public class CreateEditDeleteDeckActivity extends BaseActivity{
    private RadioGroup deckRG;
    private List<Deck> decks;
    private DeckManager deckManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_delete_deck);

        deckManager = ExternalDeckManager.getInstance(this);
        deckRG = (RadioGroup) findViewById(R.id.deckRButtons);
        populateRadioGroup(deckRG);
    }

    public void populateRadioGroup(RadioGroup deckRG){
        deckRG.clearCheck();
        deckRG.removeAllViews();
        decks = deckManager.getDecks(null);
        ListIterator<Deck> deckIterator = decks.listIterator();

        for (int i = 0; i < decks.size(); i++) {
            RadioButton currRad = new RadioButton(this);
            currRad.setId(i);
            currRad.setText(decks.get(i).getName());
            deckRG.addView(currRad);
        }
    }

    public void onCreateClicked(View view) {
        Intent intent;
        intent = new Intent(this, CreateDeckActivity.class);
        startActivity(intent);
    }

    //TODO: force the user to select a deck if they press this
    public void onDeleteClicked(View view) {
        int index = deckRG.getCheckedRadioButtonId();
        if(index == -1) {
            Toast.makeText(this, "ERROR Select a Deck", Toast.LENGTH_SHORT).show();
        } else {
            Deck selectedDeck = decks.get(index);
            selectedDeck.delete();
            selectedDeck.commit();
            populateRadioGroup(deckRG);
        }
    }

    //TODO: force the user to select a deck if they press this
    public void onEditClicked(View view) {
        int index = deckRG.getCheckedRadioButtonId();
        if(index == -1) {
            Toast.makeText(this, "ERROR Select a Deck", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent;
            intent = new Intent(this, EditDeckActivity.class);
            intent.putExtra("checkedIndex", index);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        populateRadioGroup(deckRG);
    }
}
