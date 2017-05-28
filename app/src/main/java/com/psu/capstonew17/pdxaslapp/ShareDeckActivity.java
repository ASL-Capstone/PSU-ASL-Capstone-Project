//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

import java.util.List;

public class ShareDeckActivity extends BaseActivity implements View.OnClickListener{
    private Button submit;
    private LinearLayout ll;
    private RadioGroup rg;
    private List<Deck> decks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        //Set up layout objects
        this.ll = new LinearLayout(this);
        this.rg = new RadioGroup(this);
        this.ll.setOrientation(LinearLayout.VERTICAL);
        this.submit = (Button) findViewById(R.id.button3);
        this.submit.setOnClickListener(this);

        //Get the decks from the database
        DeckManager manager = ExternalDeckManager.getInstance(this);
        this.decks = manager.getDecks(null);

        //Create Radio Buttons For Each Deck and add them to the RadioGroup
        int i = 0;
        for(Deck deck:this.decks){
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(i);
            rdbtn.setText(deck.getName());
            this.rg.addView(rdbtn);
            ++i;
        }
        ((ViewGroup)findViewById(R.id.deckList)).addView(this.rg);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button3:
                int id = this.rg.getCheckedRadioButtonId();
                if(id != -1){ //Check that the id is valid and a deck has been selected
                    Deck toShare = decks.get(id);
                    Intent intent = new Intent(this, DispQRCodeActivity.class);
                    intent.putExtra("DECKNAME", toShare.getName());
                    //Start show QR code intent
                    startActivity(intent);
                    finish();
                    return;
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.deck_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

}
