//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
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
    //called when activity is created and after a card is deleted
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
            Card card = cards.get(index);
            List<Deck> decks = card.getUsers();
            //this will hold decks that need to be deleted because they fall below card minimum
            List<Deck> decksToDelete = new ArrayList<>();
            //decks that the card will just be removed from
            List<Deck> decksToRemoveFrom = new ArrayList<>();
            //populating lists
            for (Deck curr : decks){
                if(curr.getCards().size() <= CreateEditDeleteDeckActivity.MIN_CARDS)
                    decksToDelete.add(curr);
                else
                    decksToRemoveFrom.add(curr);
            }

            //if there are decks that need to be deleted then we need to check with the user
            if(decksToDelete.size() > 0)
                decksDeletePrompt(card, decksToDelete, decksToRemoveFrom);
            //if not, we can just remove the card from the decks and delete it
            else
                deleteCard(card, decksToRemoveFrom);
        }
    }

    //removes the card from any decks that it is a member of (decks that are being deleted shouldn't
    // be in decksToRemoveFrom list)
    public void deleteCard(Card card, List<Deck> decksToRemoveFrom){
        try {
            for (Deck curr : decksToRemoveFrom) {
                curr.getCards().remove(card);
                curr.commit();
            }
            card.delete();
        //for some reason there is still a deck(s) that this card is a member of
        }catch (ObjectInUseException e) {
            Toast.makeText(this, R.string.card_in_use_error, Toast.LENGTH_SHORT).show();
        }
        //populate the radiogroup to reflect the change
        populateRadioGroup();
    }

    public void deleteDecks(List<Deck> decksToDelete){
        //deletes decks
        for (Deck curr : decksToDelete){
            curr.delete();
            curr.commit();
        }
    }

    //the message that is displayed when one or more decks need to be deleted.
    public void decksDeletePrompt(final Card card, final List<Deck> decksToDelete,
                                  final List<Deck> decksToRemoveFrom) {
        //building the message to be displayed
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.deleted_decks));
        for (int i = 0; i < decksToDelete.size(); i++){
            sb.append(decksToDelete.get(i).getName());
            if (i < decksToDelete.size() - 1)
                sb.append(", ");
        }
        //create the alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false);
        builder.setMessage(sb.toString());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            //if they want continue then go ahead and delte the decks and the card
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete the decks
                deleteDecks(decksToDelete);
                //remove the card from the other decks and delete the card
                deleteCard(card, decksToRemoveFrom);
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
}
