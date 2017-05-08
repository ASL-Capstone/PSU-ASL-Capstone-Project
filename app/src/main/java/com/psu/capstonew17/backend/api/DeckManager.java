//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;
import java.util.List;

public interface DeckManager {
    /**
     * Get a list of all decks with the given name, or all decks if name is null.
     */
    List<Deck> getDecks(String name);

    /**
     * Create a new deck with the specified name and contents.
     *
     * The deck name must be unique.
     */
    Deck buildDeck(String name, List<Card> cards) throws ObjectAlreadyExistsException;

    /**
     * Get the default deck object that all cards are added to automatically.
     *
     * The default deck cannot be deleted or modified. Any attempts to do so
     * will throw an exception.
     */
    Deck getDefaultDeck();
}
