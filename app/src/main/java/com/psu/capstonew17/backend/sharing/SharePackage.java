package com.psu.capstonew17.backend.sharing;

import android.util.Log;

import com.psu.capstonew17.backend.EncodeableObject;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Video;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
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
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // write number of cards
            os.write(ByteBuffer.allocate(4).putInt(cards.size()).array());
            EncodeableObject obj;
            for(Card card : cards){
                //encode all videos and cards
                obj = (EncodeableObject) card;
                byte[] cardBytes = obj.encodeToByteArray();
                os.write(cardBytes);
            }
            // write number of decks
            os.write(ByteBuffer.allocate(4).putInt(decks.size()).array());
            // encode all decks
            for (Deck deck : decks) {
                obj = (EncodeableObject) deck;
                byte[] deckBytes = obj.encodeToByteArray();
                os.write(deckBytes);
            }
            // write to output stream and hash
            out.write(os.toByteArray());
            hash.update(os.toByteArray());
            out.flush();
            out.close();
        }catch (Exception e){
            Log.println(Log.ERROR, "serializeTo()", e.toString());
        }
    }

    public static SharePackage deserializeFrom(InputStream in, MessageDigest hash) {
        // TODO: implement
        List<Card> cards = new ArrayList<Card>();
        List<Deck> decks = new ArrayList<Deck>();
        try {
            // decode number of cards
            byte[] numCardBytes = new byte[4];
            in.read(numCardBytes);
            int numCards = ByteBuffer.wrap(numCardBytes).getInt();
            // decode cards
            for(int i = 0; i < numCards; i++){
                byte [] cardIdBytes = new byte[4];
                in.read(cardIdBytes);
                int cardId = ByteBuffer.wrap(cardIdBytes).getInt();
                byte [] answerLengthBytes = new byte[4];
                in.read(answerLengthBytes);
                int answerLength = ByteBuffer.wrap(answerLengthBytes).getInt();
                byte [] answerBytes = new byte[answerLength];
                in.read(answerBytes);
                String answer = new String(answerBytes);
                byte [] videoLengthBytes = new byte[4];
                in.read(videoLengthBytes);
                int videoLength = ByteBuffer.wrap(videoLengthBytes).getInt();
                byte [] videoBytes = new byte[videoLength];
                in.read(videoBytes);
                // TODO: create video file and card object, add to SharePackage cards

            }
            // decode number of decks
            byte [] numDeckBytes = new byte[4];
            in.read(numDeckBytes);
            int numDecks = ByteBuffer.wrap(numDeckBytes).getInt();
            List<Integer> cardIdsInDeck;
            // decode decks
            for(int i = 0; i < numDecks; i++){
                byte [] deckIdBytes = new byte[4];
                in.read(deckIdBytes);
                int deckId = ByteBuffer.wrap(deckIdBytes).getInt();
                byte [] nameLengthBytes = new byte[4];
                in.read(nameLengthBytes);
                int nameLength = ByteBuffer.wrap(nameLengthBytes).getInt();
                byte [] nameBytes = new byte[nameLength];
                in.read(nameBytes);
                String name = new String(nameBytes);
                byte [] numCardsInDeckBytes = new byte[4];
                in.read(numCardsInDeckBytes);
                int numCardsInDeck = ByteBuffer.wrap(numCardsInDeckBytes).getInt();
                cardIdsInDeck = new ArrayList<Integer>();
                for(int c = 0; c < numCardsInDeck; c++){
                    byte [] cIdBytes = new byte[4];
                    in.read(cIdBytes);
                    int cId = ByteBuffer.wrap(cIdBytes).getInt();
                    cardIdsInDeck.add(cId);
                }
                // TODO: create deck object, add to SharePackage decks

            }
            // TODO: create and return SharePackage with list of cards and decks

        } catch (IOException e){
            Log.println(Log.ERROR, "deserializeFrom()", e.toString());
        }
        return null;
    }
}
