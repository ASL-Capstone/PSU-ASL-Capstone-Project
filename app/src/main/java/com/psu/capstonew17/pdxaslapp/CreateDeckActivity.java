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

    private DeckManager     deckManager;
    private ListView        cardListView;
    private List<Card>      allCards;
    private List<ListRow>   cardStructs;
    private EditText        textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);

        deckManager = ExternalDeckManager.getInstance(this);
        allCards    = deckManager.getDefaultDeck().getCards();
        cardStructs = new ArrayList<ListRow>();
        textBox     = (EditText) findViewById(R.id.createDeckNameField);


        //create the card structs, sets all selected values to false
        //as this is a new deck.
        for (Card curr : allCards) {
            ListRow cardStruct = new ListRow(curr.getAnswer(), false);
            cardStructs.add(cardStruct);
        }

        //set ListView options, create an instance of the CardList
        //adapter and set it as the adapter of the ListView
        cardListView = (ListView)findViewById(R.id.createCardListView);
        cardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        CustomArrayListAdapter adapter =
                new CustomArrayListAdapter(this, R.layout.item, cardStructs);
        cardListView.setAdapter(adapter);
        cardListView.setItemsCanFocus(false);
    }

    //onClick for the submit/done button.
    //TODO: constrict length of text entered in edit text
    public void onCreateSubmitClicked(View view) {
        List<Card> cardsInDeck = new ArrayList<Card>();

        for (int i = 0; i < cardStructs.size(); i++) {
            ListRow curr = cardStructs.get(i);
            if(curr.isChecked)
                cardsInDeck.add(allCards.get(i));
        }

        if(cardsInDeck.isEmpty()) {
            Toast.makeText(this, "Select at least two cards", Toast.LENGTH_SHORT).show();
        } else {
            try {
                deckManager.buildDeck(textBox.getText().toString(), cardsInDeck);
            } catch(ObjectAlreadyExistsException e) {
                Toast.makeText(this, "Deck already exists", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
