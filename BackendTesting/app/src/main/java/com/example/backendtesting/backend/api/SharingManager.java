package com.example.backendtesting.backend.api;

import com.example.backendtesting.backend.api.Card;
import com.example.backendtesting.backend.api.Deck;
import com.example.backendtesting.backend.api.SharingTransmitListener;
import java.util.List;
import android.graphics.Bitmap;

public interface SharingManager {
    class TxOptions {
        /**
         * Maximum share time in seconds
         */
        public int timeout;

        /**
         * Maximum number of people to share the data with
         */
        public int maxTargets;
    }

    /**
     * Start a sharing session with the supplied objects and options.
     *
     * @param cards List of cards to share. May be null.
     * @param decks List of decks to share. May be null.
     * @param opts The options to use when sharing
     * @param listener The transmit listener to process sharing events
     * @return The sharing QR code as a bitmap
     */
    Bitmap transmit(List<Card> cards, List<Deck> decks, TxOptions opts, SharingTransmitListener listener);

    /**
     * Start a receive session with the supplied options
     * TODO: Interface for this
     */
    void receive();
}
