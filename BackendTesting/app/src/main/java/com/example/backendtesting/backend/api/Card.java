package com.example.backendtesting.backend.api;

import java.util.List;

/**
 * Public interface for card objects.
 */
public interface Card {

    /**
     * Get the video associated with this card
     */
    Video getVideo();

    /**
     * Associate the card with a different video
     */
    void setVideo(Video v);

    /**
     * Get the card's answer
     */
    String getAnswer();

    /**
     * Change the answer associated with a card
     */
    void setAnswer(String a);

    /**
     * Delete the card. Throws an exception if the card is currently used as part of a deck.
     */
    void delete() throws ObjectInUseException;

    /**
     * Return a list of decks which currently use this card. If no decks use the card, returns an
     * empty list.
     */
    List<Deck> getUsers();
}

