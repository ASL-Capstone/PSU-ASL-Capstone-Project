//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class DeleteCardActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private ListRow row;
    private ArrayList<ListRow> list = new ArrayList<>();
    private CustomArrayAdapter myAdapter;

    private Button bttSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_card);

        bttSubmit = (Button) findViewById(R.id.button_submit);
        bttSubmit.setText("Delete");
        bttSubmit.setOnClickListener(this);

        // TODO load all cards


        // display list of cards
        listView = (ListView) findViewById(R.id.list_items);
        myAdapter =  new CustomArrayAdapter(this, 0, list);
        listView.setAdapter(myAdapter);
    }


    @Override
    public void onClick(View view) {

    }
}
