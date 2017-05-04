//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

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
     * Delete the card. Throws an exception if the card is currently used as part of any deck
     * other than the default deck.
     *
     * Once this method is called, this object is invalid and must not be used.
     * Any use of the object after calling delete() will throw an exception.
     */
    void delete() throws ObjectInUseException;

    /**
     * Return a list of decks which currently use this card.
     */
    List<Deck> getUsers();
}

