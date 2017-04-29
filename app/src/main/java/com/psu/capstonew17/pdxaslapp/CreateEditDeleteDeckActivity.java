package com.psu.capstonew17.pdxaslapp;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.testDeck;

public class CreateEditDeleteDeckActivity extends BaseActivity{
    private Button bttEdit, bttDlt, bttCrt;
    private RadioGroup deckRG;
    private List<Deck> decks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_delete_deck);

        decks = TestingStubs.manyDecks();
        ListIterator<Deck> deckIterator = decks.listIterator();
        deckRG = (RadioGroup) findViewById(R.id.deckRButtons);
        populateRadioGroup(deckRG);

    }

    public void populateRadioGroup(RadioGroup deckRG){
        deckRG.removeAllViews();
        ListIterator<Deck> deckIterator = decks.listIterator();

        while (deckIterator.hasNext()) {
            Deck curr = deckIterator.next();

            RadioButton currRad = new RadioButton(this);
            currRad.setId(deckIterator.previousIndex());
            currRad.setText(curr.getName());
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
        Deck selectedDeck = decks.get(index);
        decks.remove(index);
        deckRG.clearCheck();
        selectedDeck.delete();
        selectedDeck.commit();
        populateRadioGroup(deckRG);

    }

    //TODO: force the user to select a deck if they press this
    public void onEditClicked(View view) {
        Intent intent;
        intent = new Intent(this, EditDeckActivity.class);
        intent.putExtra("checkedIndex", deckRG.getCheckedRadioButtonId());
        startActivity(intent);
    }
}
