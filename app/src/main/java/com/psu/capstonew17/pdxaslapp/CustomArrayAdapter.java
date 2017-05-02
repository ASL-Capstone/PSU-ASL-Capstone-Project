//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Public adapter for display selectable decks in listView layout.
 **/
public class CustomArrayAdapter extends ArrayAdapter<ListRow> {
    private LayoutInflater layoutInflater;
    private CheckBox checkBox;
    private TextView textView;
    private ArrayList<ListRow> objects;

    public CustomArrayAdapter(Context context, int resource, ArrayList<ListRow> objects) {
        super(context, resource, objects);
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // check null view and declare view holder
        convertView = layoutInflater.inflate(R.layout.item, null);
        checkBox = (CheckBox) convertView.findViewById(R.id.list_row_checkBox);
        textView = (TextView) convertView.findViewById(R.id.list_row_textView);

        ListRow aRow = objects.get(position);

        textView.setText(aRow.rowName);
        if (aRow.isChecked) {
            checkBox.setChecked(true);
            View row = (View) checkBox.getParent();
            row.setBackgroundResource(R.drawable.background1);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View row = (View) buttonView.getParent();
                ListRow object = objects.get(position);
                if (isChecked) {
                    row.setBackgroundResource(R.drawable.background1);
                    object.isChecked = true;
                    objects.set(position, object);
                    Log.d("item", "Item Click at " + position + ": " + object.rowName + " is " + object.isChecked);
                } else {
                    row.setBackgroundResource(android.R.color.transparent);
                    object.isChecked = false;
                    objects.set(position, object);
                    Log.d("item", "Item Click at " + position + ": " + object.rowName + " is " + object.isChecked);
                }
            }
        });

        return convertView;
    }

    @Nullable
    @Override
    public ListRow getItem(int position) {
        return objects.get(position);
    }


    /**
     * Return a modified list of decks.
     * @return
     */
    public ArrayList<ListRow> getItems() {
        return objects;
    }

}


