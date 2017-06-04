package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import android.os.Parcel;
import android.os.Parcelable;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brook on 4/17/2017.
 */

public class testDeck implements Deck {
    protected String name;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDeckId() {
        return 0;
    }

    @Override
    public void setName(String name) throws ObjectAlreadyExistsException{
        this.name = name;
    }

    @Override
    public List<Card> getCards() {
        List<Card> ret = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            testCard first = new testCard();
            first.setAnswer("Answer" + i);
            ret.add(first);
        }
        return ret;
    }

    @Override
    public void commit() {

    }

    @Override
    public void delete() {

    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new testDeck();
        }

        @Override
        public Object[] newArray(int i) {
            return new testDeck[i];
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
