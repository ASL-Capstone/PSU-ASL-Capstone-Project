package com.psu.capstonew17.backend.api.stubs;

import android.os.Parcel;
import android.os.Parcelable;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Video;

import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class CardStub implements Card {
    private Video video;
    private String translation;
    private List<Deck> activeDecks;

    @Override
    public int getCardId() {
        return 0;
    }

    public Video getVideo() {
        return this.video;
    }

    public void setVideo(Video v) {
        this.video = v;
        return;
    }

    public String getAnswer() {
        return translation;
    }

    public void setAnswer(String s) {
        this.translation = s;
    }

    public void delete() {
        return;
    }

    public List<Deck> getUsers() {
        return activeDecks;
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new CardStub();
        }

        @Override
        public Object[] newArray(int i) {
            return new CardStub[i];
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