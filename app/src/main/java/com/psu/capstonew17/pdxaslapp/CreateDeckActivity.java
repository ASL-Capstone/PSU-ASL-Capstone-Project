//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import java.util.ArrayList;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

/**
 * Created by ichel on 4/28/2017.
 */

public class CreateDeckActivity extends BaseActivity {

    private ListView cardListView;
    private List<Card> allCards;
    private List<CardListAdapter.CardStruct> cardStructs;
    EditText textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);

        allCards = TestingStubs.manyCards();
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
    public void onCreateDoneClicked(View view) {
        List<Card> cardsInDeck = new ArrayList<Card>();
        for (CardListAdapter.CardStruct curr : cardStructs) {
            cardsInDeck.add(curr.card);
        }
        //deckManager.buildDeck(textBox.getText().toString(), cardsInDeck);
        finish();
    }
}
