//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

/**
 * Allows user to edit existing decks
 */
public class EditDeckActivity extends BaseActivity {
    private Deck            deck;
    private List<Card>      allCards;
    private List<Card>      cardsInDeck;
    private List<ListRow>   cardStructs;
    private EditText        textBox;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);

        DeckManager deckManager = ExternalDeckManager.getInstance(this);
        allCards                = deckManager.getDefaultDeck().getCards();
        cardStructs             = new ArrayList<>();

        //get the index of the selected deck to edit
        String checkedDeck = "";
        if(getIntent().hasExtra(CreateEditDeleteDeckActivity.CHECKED_DECK)) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                checkedDeck = bundle.getString(CreateEditDeleteDeckActivity.CHECKED_DECK);
            }
        }

        List<Deck>  deckList       = deckManager.getDecks(checkedDeck);

        deck        = deckList.get(0);
        cardsInDeck = deck.getCards();

        //set the edit text box to the name of the deck
        textBox = (EditText) findViewById(R.id.editDeckNameField);
        textBox.setText(deck.getName());

        //create the CardStructs and set them to selected if the specific card
        //already exists in the deck
        for (Card curr : allCards) {
            Boolean contains    = cardsInDeck.contains(curr);
            ListRow cardStruct  = new ListRow(curr.getAnswer(), contains);
            cardStructs.add(cardStruct);
        }

        //set list view option, set adapter
        ListView cardListView = (ListView)findViewById(R.id.list_items);
        cardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        CustomArrayListAdapter adapter =
                new CustomArrayListAdapter(this, R.layout.list_row, cardStructs);
        cardListView.setAdapter(adapter);
        cardListView.setItemsCanFocus(false);
    }

    //The user is done editing the deck.
    public void onEditSubmitClicked(View view) {
        String deckName = textBox.getText().toString().trim();

        //add all of the selected cards, if there are cards that are in the deck, but the user
        //deselected then they need to be removed from the deck.
        for (int i = 0; i < cardStructs.size(); i++){
            Card curr = allCards.get(i);
            if (!cardsInDeck.contains(curr) && cardStructs.get(i).isChecked) {
                cardsInDeck.add(curr);

            } else if (cardsInDeck.contains(curr) && !cardStructs.get(i).isChecked) {
                cardsInDeck.remove(curr);
            }
        }

        //is the deck name length within a valid range?
        if (TextUtils.isEmpty(deckName)
                || deckName.length() > CreateEditDeleteDeckActivity.MAX_STRG_LNGTH) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getResources().getString(R.string.deck_name_length_error));
            stringBuilder.append(CreateEditDeleteDeckActivity.MAX_STRG_LNGTH);
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();

        //are there going to be enough cards in the deck?
        } else if (cardsInDeck.size() < CreateEditDeleteDeckActivity.MIN_CARDS) {
            Toast.makeText(this, R.string.deck_size_error, Toast.LENGTH_SHORT).show();

        //everything looks good, we can create the deck.
        } else {
            if (!TextUtils.equals(deckName, deck.getName())) {
                try {
                    deck.setName(textBox.getText().toString().trim());
                    deck.commit();
                    finish();

                //darn, a deck already exists with this name!
                } catch (ObjectAlreadyExistsException e) {
                    Toast.makeText(this, R.string.deck_already_exists_error, Toast.LENGTH_SHORT).show();
                }
            } else {
                deck.commit();
                finish();
            }
        }
    }
}
