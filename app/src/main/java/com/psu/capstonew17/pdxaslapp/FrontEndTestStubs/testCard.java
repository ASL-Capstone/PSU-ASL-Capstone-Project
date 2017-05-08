package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectInUseException;
import com.psu.capstonew17.backend.api.Video;

import java.util.List;

/**
 * Created by Brook on 4/17/2017.
 */

public class testCard implements Card {
    protected Video v;
    protected String a;

    @Override
    public Video getVideo() {
        return this.v;
    }

    @Override
    public void setVideo(Video v) {
        this.v = v;
    }

    @Override
    public String getAnswer() {
        return this.a;
    }

    @Override
    public void setAnswer(String a) {
        this.a = a;
    }

    @Override
    public void delete() throws ObjectInUseException {

    }

    @Override
    public List<Deck> getUsers() {
        return null;
    }
}
