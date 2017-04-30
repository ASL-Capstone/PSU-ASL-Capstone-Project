//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class CreateCardActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private ListRow row;
    private ArrayList<ListRow> list = new ArrayList<>();
    private CustomArrayAdapter myAdapter;

    private Button bttSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        String[] strs = {"deck 1", "deck 2", "deck 3", "deck 4"};

        for (String str: strs) {
            row = new ListRow();
            row.name = str;
            row.isChecked = false;
            list.add(row);
        }
        listView = (ListView) findViewById(R.id.list_decks);
        myAdapter =  new CustomArrayAdapter(this, 0, list);
        listView.setAdapter(myAdapter);

        bttSubmit = (Button) findViewById(R.id.button_submit);
        bttSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.button_submit:
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

