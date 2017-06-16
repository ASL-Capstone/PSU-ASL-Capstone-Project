//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Use this as the listview adapter.
 */

class CustomArrayListAdapter extends ArrayAdapter<ListRow> {
    private Context         context;
    private List<ListRow>   rows;

    CustomArrayListAdapter(Context context, int tvResId, List<ListRow> rows){
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
    public @NonNull View getView(final int index, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            //inflate layout
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);

            //create the view holder
            viewHolder          = new ViewHolder();
            viewHolder.name     = (TextView) convertView.findViewById(R.id.list_row_textView);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.list_row_checkBox);
            viewHolder.name.setText(rows.get(index).name);
            viewHolder.checkBox.setChecked(rows.get(index).isChecked);

            //view set tags
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.list_row_textView, viewHolder.name);
            convertView.setTag(R.id.list_row_checkBox, viewHolder.checkBox);

        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //onClickListener for the checkboxes,
        //sets toggles the selected field of the CardStruct
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListRow selectedRow = rows.get(index);
                selectedRow.isChecked = ((CheckBox) v).isChecked();
            }
        });
        viewHolder.checkBox.setChecked(rows.get(index).isChecked);
        viewHolder.name.setText(rows.get(index).name);

        //set viewholder tags

        return convertView;
    }


}
