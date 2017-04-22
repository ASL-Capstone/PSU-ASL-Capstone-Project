package com.psu.capstonew17.pdxaslapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class CreateCardActivity extends AppCompatActivity {
    private ListView listView;
    private ListRow row;
    private ArrayList<ListRow> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        String[] strs = {"deck 1", "deck deck 2", "this is deck 3", "deck 4"};

        for (String str: strs) {
            row = new ListRow();
            row.name = str;
            row.isChecked = false;
            list.add(row);
        }
        listView = (ListView) findViewById(R.id.list_decks);
        listView.setAdapter(new CustomArrayAdapter(this, 0, list));
    }
}

