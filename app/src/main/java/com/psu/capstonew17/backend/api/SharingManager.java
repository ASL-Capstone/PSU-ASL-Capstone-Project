//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;
import android.graphics.Bitmap;

import java.util.List;

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

    class RxOptions {
        /**
         * Maximum retries
         */
        public int retries;
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
     *
     * @param code The text content of the detected QR code
     * @param opts Receive options
     * @param listener Handler object to get notifications about the receive process
     */
    void receive(String code, RxOptions opts, SharingReceiveListener listener);
}
