package com.psu.capstonew17.backend.api;

import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class CardStub implements Card{
    private Video video;
    private String translation;
    private List<Deck> activeDecks;

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
}