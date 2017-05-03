//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

import java.util.ArrayList;
import java.util.List;

public class CreateCardActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private ListRow row;
    private ArrayList<ListRow> listRows = new ArrayList<>();
    private CustomArrayAdapter myAdapter;

    private Button bttSubmit;
    private Button bttGetVideo;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        // Declare layout components
        bttSubmit = (Button) findViewById(R.id.button_submit);
        bttGetVideo = (Button) findViewById(R.id.button_get_video);
        bttSubmit.setText("Create");
        editText = (EditText) findViewById(R.id.edit_text_video_description);

        // Enable on click listener
        bttSubmit.setOnClickListener(this);
        bttGetVideo.setOnClickListener(this);
        editText.setOnClickListener(this);

        //ArrayList<Deck> decksList = new ArrayList<>(ExternalDeckManager.getInstance(this).getDecks(null));

        //get Decks from current testing
        ArrayList<Deck> deckList = new ArrayList<>(TestingStubs.manyDecks());

        for (int i = 0; i < deckList.size(); ++i) {
            ListRow listRow = new ListRow(deckList.get(i).getName() , false);
            listRows.add(listRow);
        }

        listView = (ListView) findViewById(R.id.list_items);
        myAdapter =  new CustomArrayAdapter(this, 0, listRows);
        listView.setAdapter(myAdapter);


    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {
            case R.id.button_get_video:
                intent = new Intent(getApplicationContext(), EditVideoActivity.class);
                startActivity(intent);

                break;

            case R.id.button_submit:
                // Check for video + video answer

                // Check for decks selection,


                String pos = "";

                // TODO handle add new card to selected decks
                ArrayList<ListRow> objects = myAdapter.getItems();
                int count = 0;
                for (ListRow object: objects) {
                    if (object.isChecked)
                        pos += count + "; ";
                    count++;
                }

                Log.d("click", "Clicked on: " + pos);

                break;
        }
    }


}

