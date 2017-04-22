package com.psu.capstonew17.pdxaslapp;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.ArrayList;


public class CustomArrayAdapter extends ArrayAdapter<ListRow> {
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private ArrayList<ListRow> objects;

    public CustomArrayAdapter(Context context, int resource, ArrayList<ListRow> objects) {
        super(context, resource, objects);
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // check null view and declare view holder
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.list_row_checkBox);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.list_row_textView);

            // enable click listener on checkbox
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        ListRow row = getItem(position);
        Log.d("size", row.name);
        viewHolder.checkBox.setChecked(row.isChecked);
        viewHolder.checkBox.setTag(position);

        // Set state




        return convertView;
    }

    @Nullable
    @Override
    public ListRow getItem(int position) {
        return objects.get(position);
    }

    private class ViewHolder {
        public CheckBox checkBox;
        public TextView textView;
    }

    public boolean isChecked(int index) {
        return objects.get(index).isChecked;
    }

//    @Override
//    public void onClick(View view) {
//        ListRow row = objects.get((Integer)view.getTag());
//
//        row.isChecked = true;
//
//        Log.d("item", "Item Click at "+(Integer)view.getTag()+" : "+row.name +" is "+row.isChecked);
//    }
}


