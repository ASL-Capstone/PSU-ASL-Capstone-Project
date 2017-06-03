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

import java.util.ArrayList;
import java.util.List;

public class DeleteCardActivity extends BaseActivity {
    private List<Card>  cards;
    private DeckManager deckManager;
    private RadioGroup  cardRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_card);

        deckManager = ExternalDeckManager.getInstance(this);
        cards       = deckManager.getDefaultDeck().getCards();
        cardRG      = (RadioGroup) findViewById(R.id.cardRButtons);
        populateRadioGroup();
    }

    //clear the checked card, populate the radiogroup again
    //called after a card is deleted
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

    //user wants to delete that card they checked.
    public void onDeleteClicked(View view) {
        int index = cardRG.getCheckedRadioButtonId();
        //If the checked index is -1 the user never selected a card to delete. Silly user.
        if (index == -1) {
            Toast.makeText(this, R.string.select_card, Toast.LENGTH_SHORT).show();
        } else {
            try {
                Card card = cards.get(index);
                List<Deck> decks = card.getUsers();
                List<String> deletedDecks = new ArrayList<>();
                //we have to either remove the card from every deck it is in, or delete the deck
                //if it will have less than 2 cards in it after the card is removed
                for (Deck curr : decks){
                    List<Card> cardsInDeck = curr.getCards();
                    if(cardsInDeck.size() > 2)
                        cardsInDeck.remove(card);
                    else {
                        deletedDecks.add(curr.getName());
                        curr.delete();
                    }
                    curr.commit();
                }
                //now we can (hopefully) delete the card
                card.delete();
                //did we delete any decks? let's let the user know
                if(!deletedDecks.isEmpty())
                    decksDeletedMsg(deletedDecks);
                //repopulate the radiogroup so that it's still accurate
                populateRadioGroup();

            } catch(ObjectInUseException e) {
                //poo, something went wrong. card was still a member of a deck.
                Toast.makeText(this, R.string.card_in_use_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //the message that is displayed when one or more decks are deleted.
    public void decksDeletedMsg(List<String> deletedDecks) {
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.deleted_decks));
        for (int i = 0; i < deletedDecks.size(); i++){
            sb.append(deletedDecks.get(i));
            if (i < deletedDecks.size() - 1)
                sb.append(", ");
        }
        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
    }
}
