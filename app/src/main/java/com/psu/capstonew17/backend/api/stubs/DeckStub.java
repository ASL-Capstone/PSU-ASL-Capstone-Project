package com.psu.capstonew17.backend.api.stubs;

import android.os.Parcel;
import android.os.Parcelable;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;

import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class DeckStub implements Deck {
    private String name;
    private List<Card> cards;

    public String getName() {
        return this.name;
    }

    @Override
    public int getDeckId() {
        return 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void commit() {
        //Updates the list
    }

    public void delete() {
        //Delete deck
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new DeckStub();
        }

        @Override
        public Object[] newArray(int i) {
            return new DeckStub[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
