//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

import android.os.Parcelable;
import java.util.List;

/**
 * Public interface for mutable decks of cards.
 */
public interface Deck extends Parcelable {
    /**
     * Get the deck's title
     */
    String getName();

    /**
     * Get the deck's id
     */
    int getDeckId();

    /**
     * Modify the deck's title. If the new title is already being used
     * by another deck, an exception will be thrown.
     */
    void setName(String name) throws ObjectAlreadyExistsException;

    /**
     * Get all cards in the deck as an list. The returned list is an isolated copy, which is unique
     * to the given Deck object. Changes will not be reflected in the database until you call the
     * commit() method.
     *
     * @return A list of cards in the deck
     */
    List<Card> getCards();

    /**
     * Update the database with any changes to the internal list.
     */
    void commit();

    /**
     * Delete this deck.
     *
     * Once this method is called, this object is invalid and must not be used.
     * Any use of the object after calling delete() will throw an exception.
     */
    void delete();
}
