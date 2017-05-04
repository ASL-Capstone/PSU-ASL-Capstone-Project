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
    private Context context;
    private CheckBox checkBox;
    private TextView textView;
    private ArrayList<ListRow> objects;

    public CustomArrayAdapter(Context context, int resource, ArrayList<ListRow> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RowViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);

            viewHolder = new RowViewHolder();
            viewHolder.checkBox = (CheckBox)
                    convertView.findViewById(R.id.list_row_checkBox);
            viewHolder.textView = (TextView)
                    convertView.findViewById(R.id.list_row_textView);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//                    TextView label = (TextView) v.getTag(R.id.cardName);
                    View row = (View) v.getParent();
                    row.setBackgroundResource(R.drawable.background2);
                    ListRow selectedDeck = objects.get(position);
                    if(((CheckBox) v).isChecked())
                        selectedDeck.isChecked = true;
                    else
                        selectedDeck.isChecked = false;

                    objects.set(position, selectedDeck);
                }
            });

            // store holder of view
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.list_row_textView, viewHolder.textView);
            convertView.setTag(R.id.list_row_checkBox, viewHolder.checkBox);

        } else {
            viewHolder = (RowViewHolder) convertView.getTag();
        }

        viewHolder.checkBox.setTag(position);
        viewHolder.textView.setText(objects.get(position).name);
        viewHolder.checkBox.setChecked(objects.get(position).isChecked);

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


