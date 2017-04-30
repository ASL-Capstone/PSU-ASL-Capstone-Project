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

public class CardListAdapter extends ArrayAdapter<CardListAdapter.CardStruct> {
    Context context;
    List<CardStruct> cardStructs;

    public CardListAdapter(Context context, int tvResId, List<CardStruct> cards){
        super(context, tvResId, cards);
        this.context = context;
        this.cardStructs = new ArrayList<CardStruct>();
        this.cardStructs.addAll(cards);
    }

    private class CardCB {
        TextView name;
        CheckBox checkBox;
    }

    //creates/gets the view for the List
    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        CardCB cardCB = null;

        if(convertView == null){
            //inflate layout
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.card_list_item, null);

            //create the view holder
            cardCB = new CardCB();
            cardCB.name = (TextView) convertView.findViewById(R.id.cardName);
            cardCB.checkBox = (CheckBox) convertView.findViewById(R.id.cardCheckbox);

            //onClickListener for the checkboxes,
            //sets toggles the selected field of the CardStruct
            cardCB.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    TextView label = (TextView) v.getTag(R.id.cardName);
                    CardStruct selectedCard = cardStructs.get(index);
                    if(((CheckBox) v).isChecked())
                        selectedCard.selected = true;
                    else
                        selectedCard.selected = false;
                }
            });

            //view set tags
            convertView.setTag(cardCB);
            convertView.setTag(R.id.cardName, cardCB.name);
            convertView.setTag(R.id.cardCheckbox, cardCB.checkBox);

        } else
            cardCB = (CardCB) convertView.getTag();

        //set viewholder tags
        cardCB.checkBox.setTag(index);
        cardCB.name.setText(cardStructs.get(index).card.getAnswer());
        cardCB.checkBox.setChecked(cardStructs.get(index).selected);

        return convertView;
    }

    //crappy struct to store a card and if the card is selected
    static public class CardStruct {
        public Card card;
        public boolean selected;

        CardStruct(Card card, Boolean selected){
            this.card = card;
            this.selected = selected;
        }
    }
}
