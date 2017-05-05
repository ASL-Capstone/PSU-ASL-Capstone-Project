//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.List;

/**
 * Public adapter for display selectable decks in listView layout.
 **/
public class CustomArrayAdapter extends ArrayAdapter<ListRow> {
    private Context context;
    private List<ListRow> rows;

    public CustomArrayAdapter(Context context, int resource, List<ListRow> rows) {
        super(context, resource, rows);
        this.context = context;
        this.rows = rows;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            rows.get((Integer)buttonView.getTag()).isChecked = isChecked;
        }
    };

    public class RowViewHolder {
        CheckBox checkBox;
        TextView textView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RowViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        viewHolder = new RowViewHolder();
        viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.list_row_checkBox);
        viewHolder.textView = (TextView) convertView.findViewById(R.id.list_row_textView);

        viewHolder.textView.setText(getItem(position).name);
        viewHolder.checkBox.setTag(Integer.valueOf(position));
        viewHolder.checkBox.setChecked(getItem(position).isChecked);
        viewHolder.checkBox.setOnCheckedChangeListener(mListener);

        return convertView;
    }

    @Nullable
    @Override
    public ListRow getItem(int position) {
        return rows.get(position);
    }
}


