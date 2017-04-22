package com.example.backendtesting.backend.api;

import java.util.List;
/**
 * Created by Tim on 4/21/2017.
 */

public class CardStubs implements Card {

    public Video video;
    public String translation;
    public List<Deck> activeDecks;

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
