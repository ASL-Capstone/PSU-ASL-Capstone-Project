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

import com.google.zxing.common.StringUtils;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.data.ExternalDeckManager;


/**
 * Created by ichel on 4/28/2017.
 * allows the user to create new decks
 */

public class CreateDeckActivity extends BaseActivity {

    private DeckManager     deckManager;
    private List<Card>      allCards;
    private List<ListRow>   cardStructs;
    private EditText        textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);

        deckManager = ExternalDeckManager.getInstance(this);
        allCards    = deckManager.getDefaultDeck().getCards();
        cardStructs = new ArrayList<>();
        textBox     = (EditText) findViewById(R.id.createDeckNameField);


        //create the card structs, sets all selected values to false
        //as this is a new deck.
        for (Card curr : allCards) {
            ListRow cardStruct = new ListRow(curr.getAnswer(), false);
            cardStructs.add(cardStruct);
        }

        //set ListView options, create an instance of the CardList
        //adapter and set it as the adapter of the ListView
        ListView cardListView = (ListView)findViewById(R.id.list_items);
        cardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        CustomArrayListAdapter adapter =
                new CustomArrayListAdapter(this, R.layout.list_row, cardStructs);
        cardListView.setAdapter(adapter);
        cardListView.setItemsCanFocus(false);
    }

    //onClick for the submit/done button.
    public void onCreateSubmitClicked(View view) {
        List<Card> cardsInDeck = new ArrayList<>();
        String deckName = textBox.getText().toString().trim();

        //go through the list for the list view and add any cards that are selected
        for (int i = 0; i < cardStructs.size(); i++) {
            ListRow curr = cardStructs.get(i);
            if(curr.isChecked)
                cardsInDeck.add(allCards.get(i));
        }

        //is the deck name long/short enough?
        if (TextUtils.isEmpty(deckName)
                || deckName.length() > CreateEditDeleteDeckActivity.MAX_STRG_LNGTH) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getResources().getString(R.string.deck_name_length_error));
            stringBuilder.append(CreateEditDeleteDeckActivity.MAX_STRG_LNGTH);
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();

        //are there enough elected cards?
        } else if (cardsInDeck.size() < CreateEditDeleteDeckActivity.MIN_CARDS) {
            Toast.makeText(this, R.string.deck_size_error, Toast.LENGTH_SHORT).show();

        //everything looks good, we can create the deck
        } else {
            try {
                deckManager.buildDeck(textBox.getText().toString().trim(), cardsInDeck);
                finish();

            //Poo, the deck already exists. This means that the name the user entered isn't unique.
            } catch(ObjectAlreadyExistsException e) {
                Toast.makeText(this, R.string.deck_already_exists_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
