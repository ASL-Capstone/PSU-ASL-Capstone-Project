//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

import java.util.ArrayList;

public class DeleteCardActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private ArrayList<ListRow> listRows = new ArrayList<>();
    private CustomArrayAdapter myAdapter;

    private Button bttSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_card);

        bttSubmit = (Button) findViewById(R.id.button_submit);
        bttSubmit.setText("Delete");
        bttSubmit.setOnClickListener(this);

        //ArrayList<Deck> decksList = new ArrayList<>(ExternalDeckManager.getInstance(this).getDecks(null));

        //get Decks from current testing
        ArrayList<Card> cardList = new ArrayList<>(TestingStubs.manyCards());

        for (int i = 0; i < cardList.size(); ++i) {
            ListRow listRow = new ListRow(cardList.get(i).getAnswer() , false);
            listRows.add(listRow);
        }

        listView = (ListView) findViewById(R.id.list_items);
        myAdapter =  new CustomArrayAdapter(this, 0, listRows);
        listView.setAdapter(myAdapter);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_submit:
                listRows = myAdapter.getItems();

                for (ListRow row: listRows) {
                    if (row.isChecked) {
                        // TODO delete card, call back end api to delete
                    }
                }

                break;

            default:
                break;
        }
    }
}
