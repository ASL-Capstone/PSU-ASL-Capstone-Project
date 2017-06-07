//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import java.util.ArrayList;
import java.util.List;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
public class CreateEditDeckActivity extends BaseActivity implements View.OnClickListener {
    private Deck            deck;
    private List<Card>      allCards;
    private List<Card>      cardsInDeck;
    private List<ListRow>   cardStructs;
    private EditText        textBox;
    private boolean         editMode;
    private DeckManager     deckManager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_deck);
        Button subButton    = (Button) findViewById(R.id.bttn_submit);
        subButton.setOnClickListener(this);
        textBox     = (EditText) findViewById(R.id.deckNameField);
        deckManager = ExternalDeckManager.getInstance(this);
        allCards    = deckManager.getDefaultDeck().getCards();
        cardStructs = new ArrayList<>();
        textBox.setHint(R.string.CreateDeck_DeckName);

        //if there's a bundle to grab, then the user is editing a deck
        if(getIntent().hasExtra(CreateEditDeleteDeckActivity.CHECKED_DECK)) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                //get the selected deck to edit
                deck = deckManager.getDecks(bundle.getString(CreateEditDeleteDeckActivity.CHECKED_DECK)).get(0);
                //get all the card in the deck the user is editing
                cardsInDeck = deck.getCards();
                //this will affect what happens when the user hits submit or create
                //in this case the deck will be updated and commited
                editMode = true;
                //button should say "submit" if the user is editing
                subButton.setText(R.string.button_submit);
                //set the edit text box to the name of the deck
                textBox.append(deck.getName());
            }
        //if not, then they're creating a deck
        } else {
            //this is a new deck, so there aren't any card currently
            cardsInDeck = new ArrayList<>();
            //since this is being set to false a new deck will be created when the user hits create/submit
            editMode    = false;
            //the user is creating, so the button should say "create"
            subButton.setText(R.string.button_create);
        }

        //create the CardStructs and set them to selected if the specific card
        //already exists in the deck. fi this is a new deck then they will all be set to false
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
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bttn_submit:
                String deckName = textBox.getText().toString().trim();

                //add all of the selected cards, if there are cards that are in the deck, but the user
                //deselected them, then they need to be removed from the deck.
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

                //everything looks good, we can create or edit the deck.
                } else {
                    try {
                        if (editMode) {
                            deck.setName(textBox.getText().toString().trim());
                            deck.commit();
                        } else {
                            deckManager.buildDeck(textBox.getText().toString().trim(), cardsInDeck);
                        }
                        finish();

                    //darn, a deck already exists with this name!
                    } catch (ObjectAlreadyExistsException e) {
                        Toast.makeText(this, R.string.deck_already_exists_error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
