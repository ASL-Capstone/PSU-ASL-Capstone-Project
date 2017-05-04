//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.psu.capstonew17.backend.api.Card;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ichel on 4/29/2017.
 */

public class CustomArrayListAdapter extends ArrayAdapter<ListRow> {
    Context         context;
    List<ListRow>   rows;

    public CustomArrayListAdapter(Context context, int tvResId, List<ListRow> rows){
        super(context, tvResId, rows);
        this.context = context;
        this.rows    = rows;
    }

    private class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    //creates/gets the view for the List
    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            //inflate layout
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, null);

            //create the view holder
            viewHolder          = new ViewHolder();
            viewHolder.name     = (TextView) convertView.findViewById(R.id.list_row_textView);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.list_row_checkBox);

            //onClickListener for the checkboxes,
            //sets toggles the selected field of the CardStruct
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    TextView label = (TextView) v.getTag(R.id.list_row_textView);
                    ListRow selectedRow = rows.get(index);
                    if (((CheckBox) v).isChecked())
                        selectedRow.isChecked = true;
                    else
                        selectedRow.isChecked = false;
                }
            });

            //view set tags
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.list_row_textView, viewHolder.name);
            convertView.setTag(R.id.list_row_checkBox, viewHolder.checkBox);

        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //set viewholder tags
        viewHolder.checkBox.setTag(index);
        viewHolder.name.setText(rows.get(index).name);
        viewHolder.checkBox.setChecked(rows.get(index).isChecked);

        return convertView;
    }


}
