package com.psu.capstonew17.backend.sharing;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.psu.capstonew17.backend.EncodeableObject;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalVideoManager;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of objects to be serialized/deserialized
 */
class SharePackage implements Parcelable{
    private List<Card> cards;
    private List<Deck> decks;

    public SharePackage(List<Card> cards, List<Deck> decks) {
        this.cards = cards;
        this.decks = decks;
    }

    public void serializeTo(OutputStream out, MessageDigest hash) {
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
            e.printStackTrace();
        }
    }

    public static SharePackage deserializeFrom(InputStream in, MessageDigest hash, Context context) {
        List<Card> cards = new ArrayList<Card>();
        List<Deck> decks = new ArrayList<Deck>();
        Map<Integer, Card> idToCardMap = new HashMap<Integer, Card>();
        SharePackage sharePackage = null;
        ExternalVideoManager videoManager = (ExternalVideoManager) ExternalVideoManager.getInstance(context);
        ExternalCardManager cardManager = (ExternalCardManager) ExternalCardManager.getInstance(context);
        ExternalDeckManager deckManager = (ExternalDeckManager) ExternalDeckManager.getInstance(context);
        try {
            // decode number of cards
            byte[] numCardBytes = new byte[4];
            in.read(numCardBytes);
            int numCards = ByteBuffer.wrap(numCardBytes).getInt();
            hash.update(numCardBytes);
            byte [] cardIdBytes = new byte[4];
            byte [] answerLengthBytes = new byte[4];
            byte [] videoLengthBytes = new byte[4];
            byte [] answerBytes;
            byte [] videoBytes;
            // decode cards
            for(int i = 0; i < numCards; i++){
                in.read(cardIdBytes);
                hash.update(cardIdBytes);
                in.read(answerLengthBytes);
                hash.update(answerLengthBytes);
                answerBytes = new byte[ByteBuffer.wrap(answerLengthBytes).getInt()];
                in.read(answerBytes);
                hash.update(answerBytes);
                in.read(videoLengthBytes);
                hash.update(videoLengthBytes);
                videoBytes = new byte[ByteBuffer.wrap(videoLengthBytes).getInt()];
                in.read(videoBytes);
                hash.update(videoBytes);
                // create video file and card object, add to SharePackage cards
                Video video = videoManager.decodeVideo(videoBytes);
                try {
                    Card card = cardManager.buildCard(video, new String(answerBytes));
                    cards.add(card);
                    idToCardMap.put(ByteBuffer.wrap(cardIdBytes).getInt(), card);
                } catch (ObjectAlreadyExistsException e) {
                    Log.println(Log.INFO, "obj already exists", e.toString());
                }
            }
            // decode number of decks
            byte [] numDeckBytes = new byte[4];
            in.read(numDeckBytes);
            hash.update(numDeckBytes);
            int numDecks = ByteBuffer.wrap(numDeckBytes).getInt();
            List<Card> cardsInDeck;
            byte [] deckIdBytes = new byte[4];
            byte [] nameLengthBytes = new byte[4];
            byte [] numCardsInDeckBytes = new byte[4];
            byte [] cIdBytes = new byte[4];
            byte [] nameBytes;
            // decode decks
            for(int i = 0; i < numDecks; i++){
                in.read(deckIdBytes);
                hash.update(deckIdBytes);
                in.read(nameLengthBytes);
                hash.update(nameLengthBytes);
                nameBytes = new byte[ByteBuffer.wrap(nameLengthBytes).getInt()];
                in.read(nameBytes);
                hash.update(nameBytes);
                in.read(numCardsInDeckBytes);
                hash.update(numCardsInDeckBytes);
                cardsInDeck = new ArrayList<Card>();
                for(int c = 0; c < ByteBuffer.wrap(numCardsInDeckBytes).getInt(); c++){
                    in.read(cIdBytes);
                    hash.update(cIdBytes);
                    Card existingCard = idToCardMap.get(ByteBuffer.wrap(cIdBytes).getInt());
                    if(existingCard != null){
                        cardsInDeck.add(existingCard);
                    }
                }
                // create deck object, add to SharePackage decks
                try {
                    Deck deck = deckManager.buildDeck(new String(nameBytes), cardsInDeck);
                    decks.add(deck);
                } catch (ObjectAlreadyExistsException e) {
                    Log.println(Log.INFO, "obj already exists", e.toString());
                }
            }
            sharePackage = new SharePackage(cards, decks);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sharePackage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.cards);
        parcel.writeList(this.decks);
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            List<Card> cards = new ArrayList<Card>();
            parcel.readList(cards, Card.class.getClassLoader());
            List<Deck> decks = new ArrayList<Deck>();
            parcel.readList(decks, Deck.class.getClassLoader());
            return new SharePackage(cards, decks);
        }

        @Override
        public Object[] newArray(int i) {
            return new SharePackage[i];
        }
    };
}
