//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import java.util.ArrayList;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.data.ExternalDeckManager;


/**
 * Created by ichel on 4/28/2017.
 */

public class CreateDeckActivity extends BaseActivity {

    private DeckManager deckManager;
    private ListView cardListView;
    private List<Card> allCards;
    private List<CardListAdapter.CardStruct> cardStructs;
    private EditText textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);

        deckManager = ExternalDeckManager.getInstance(this);
        //TODO: backend integration to get all cards
        allCards = deckManager.getDefaultDeck().getCards();
        cardStructs = new ArrayList<CardListAdapter.CardStruct>();
        textBox = (EditText) findViewById(R.id.createDeckNameField);


        //create the card structs, sets all selected values to false
        //as this is a new deck.
        for (Card curr : allCards) {
            CardListAdapter.CardStruct cardStruct = new CardListAdapter.CardStruct(curr, false);
            cardStructs.add(cardStruct);
        }

        //set ListView options, create an instance of the CardList
        //adapter and set it as the adapter of the ListView
        cardListView = (ListView)findViewById(R.id.createCardListView);
        cardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        CardListAdapter adapter =
                new CardListAdapter(this, R.layout.card_list_item, cardStructs);
        cardListView.setAdapter(adapter);
        cardListView.setItemsCanFocus(false);
    }

    //onClick for the submit/done button.
    //needs to be integrated with backend
    //TODO: integration with back end, constrict length of text entered in edit text
    //TODO: force user to select at least one card
    public void onCreateSubmitClicked(View view) {
        List<Card> cardsInDeck = new ArrayList<Card>();

        for (CardListAdapter.CardStruct curr : cardStructs) {
            if(curr.selected)
                cardsInDeck.add(curr.card);
        }

        if(cardsInDeck.isEmpty()) {
            Toast.makeText(this, "ERROR Select at least one card", Toast.LENGTH_SHORT).show();
        } else {
            try {
                deckManager.buildDeck(textBox.getText().toString(), cardsInDeck);
            } catch(ObjectAlreadyExistsException e) {

            }
            finish();
        }
    }
}
