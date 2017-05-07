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
 * Created by ichel on 4/28/2017.
 * Allows user to edit existing decks
 */

public class EditDeckActivity extends BaseActivity {
    private Deck            deck;
    private List<Card>      allCards;
    private List<Card>      cardsInDeck;
    private List<ListRow>   cardStructs;
    private EditText        textBox;
    private final String    CHECKED_INDEX   = "checkedIndex";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);

        DeckManager deckManager = ExternalDeckManager.getInstance(this);
        List<Deck>  decks       = deckManager.getDecks(null);
        allCards                = deckManager.getDefaultDeck().getCards();
        cardStructs             = new ArrayList<>();

        //get the index of the selected deck to edit
        int checkedIndex = 0;
        if(getIntent().hasExtra(CHECKED_INDEX)) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                checkedIndex = bundle.getInt(CHECKED_INDEX);
            }
        }

        deck        = decks.get(checkedIndex);
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
        ListView cardListView = (ListView)findViewById(R.id.editCardListView);
        cardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        CustomArrayListAdapter adapter =
                new CustomArrayListAdapter(this, R.layout.list_row, cardStructs);
        cardListView.setAdapter(adapter);
        cardListView.setItemsCanFocus(false);
    }

    //TODO: constrict length of text entered in edit text
    public void onEditSubmitClicked(View view) {
        String deckName = textBox.getText().toString().trim();

        for (int i = 0; i < cardStructs.size(); i++){
            Card curr = allCards.get(i);
            if (!cardsInDeck.contains(curr) && cardStructs.get(i).isChecked) {
                cardsInDeck.add(curr);

            } else if (cardsInDeck.contains(curr) && !cardStructs.get(i).isChecked) {
                cardsInDeck.remove(curr);
            }
        }

        if (TextUtils.isEmpty(deckName)
                || deckName.length() > CreateEditDeleteDeckActivity.MAX_STRG_LNGTH) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getResources().getString(R.string.deck_name_length_error));
            stringBuilder.append(CreateEditDeleteDeckActivity.MAX_STRG_LNGTH);
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();


        } else if (cardsInDeck.size() < CreateEditDeleteDeckActivity.MIN_CARDS) {
            Toast.makeText(this, R.string.deck_size_error, Toast.LENGTH_SHORT).show();

        } else {
            if (!TextUtils.equals(deckName, deck.getName())) {
                try {
                    deck.setName(textBox.getText().toString());
                    deck.commit();
                    finish();
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
