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
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

/**
 * Created by ichel on 4/28/2017.
 */

public class EditDeckActivity extends BaseActivity {

    private ListView cardListView;
    private Deck deck;
    private List<Card> allCards;
    private List<Card> cardsInDeck;
    private List<CardListAdapter.CardStruct> cardStructs;
    EditText textBox;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);

        List<Deck> decks = TestingStubs.manyDecks();
        allCards = TestingStubs.manyCards();
        cardStructs = new ArrayList<CardListAdapter.CardStruct>();

        //get the index of the selected deck to edit
        int checkedIndex = 0;
        if(getIntent().hasExtra("checkedIndex")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                checkedIndex = bundle.getInt("checkedIndex");
            }
        }

        deck = decks.get(checkedIndex);
        cardsInDeck = deck.getCards();

        //set the edit text box to the name of the deck
        textBox = (EditText) findViewById(R.id.editDeckNameField);
        textBox.setText(deck.getName());

        //create the CardStructs and set them to selected if the specific card
        //already exists in the deck
        for (Card curr : allCards) {
            Boolean contains = (cardsInDeck.contains(curr))? true : false;

            CardListAdapter.CardStruct cardStruct = new CardListAdapter.CardStruct(curr, contains);
            cardStructs.add(cardStruct);
        }

        //set list view option, set adapter
        cardListView = (ListView)findViewById(R.id.editCardListView);
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
    public void onEditDoneClicked(View view) {
        List<Card> cardsInDeck = new ArrayList<Card>();
        for (CardListAdapter.CardStruct curr : cardStructs) {
            if(!cardsInDeck.contains(curr) && curr.selected) {
                //need way to add cards to an existing deck.
                //no method in backend api.
            }
        }
        deck.setName(textBox.getText().toString());
        deck.commit();
        finish();
    }
}
