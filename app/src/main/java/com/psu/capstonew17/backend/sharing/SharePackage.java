package com.psu.capstonew17.backend.sharing;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Video;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.List;

/**
 * A set of objects to be serialized/deserialized
 */
class SharePackage {
    private List<Card> cards;
    private List<Deck> decks;

    public SharePackage(List<Card> cards, List<Deck> decks) {
        this.cards = cards;
        this.decks = decks;
    }

    private Video createVideo(int id, File vfile) {
        // TODO: implement this
        return null;
    }

    public void serializeTo(OutputStream out, MessageDigest hash) {
        // TODO: implement
        // encode all videos

        // encode all cards

        // encode all decks
    }

    public static SharePackage deserializeFrom(InputStream in, MessageDigest hash) {
        // TODO: implement
        // decode videos

        // decode cards

        // decode decks

        return null;
    }
}
