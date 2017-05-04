//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.ObjectInUseException;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

import java.util.List;

public class DeleteCardActivity extends BaseActivity {
    private List<Card>  cards;
    private DeckManager deckManager;
    private RadioGroup  cardRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_card);

        //get Decks from current testing
        deckManager = ExternalDeckManager.getInstance(this);
        cards       = deckManager.getDefaultDeck().getCards();
        cardRG      = (RadioGroup) findViewById(R.id.cardRButtons);
        populateRadioGroup();
    }

    public void populateRadioGroup(){
        cardRG.clearCheck();
        cardRG.removeAllViews();
        cards = deckManager.getDefaultDeck().getCards();
        for (int i = 0; i < cards.size(); i++) {
            RadioButton currRad = new RadioButton(this);
            currRad.setId(i);
            currRad.setText(cards.get(i).getAnswer());
            cardRG.addView(currRad);
        }
    }

    public void onDeleteClicked(View view) {
        int index = cardRG.getCheckedRadioButtonId();
        if (index == -1) {
            Toast.makeText(this, "Select a Deck", Toast.LENGTH_SHORT).show();
        } else {
            Card card = cards.get(index);
            List<Deck> decks = card.getUsers();
            for (Deck curr : decks){
                List<Card> cardsInDeck = curr.getCards();
                cardsInDeck.remove(card);
                curr.commit();
            }

            try {
                card.delete();
                populateRadioGroup();
            } catch(ObjectInUseException e) {
                Toast.makeText(this, "Error: card still exists in deck", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
