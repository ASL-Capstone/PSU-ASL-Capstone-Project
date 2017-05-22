package com.psu.capstonew17.backend.sharing;

import android.content.Context;
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
class SharePackage {
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
        ExternalVideoManager vm = (ExternalVideoManager) ExternalVideoManager.getInstance(context);
        ExternalCardManager cm = (ExternalCardManager) ExternalCardManager.getInstance(context);
        ExternalDeckManager dm = (ExternalDeckManager) ExternalDeckManager.getInstance(context);
        try {
            // decode number of cards
            byte[] numCardBytes = new byte[4];
            in.read(numCardBytes);
            int numCards = ByteBuffer.wrap(numCardBytes).getInt();
            hash.update(numCardBytes);
            // decode cards
            for(int i = 0; i < numCards; i++){
                byte [] cardIdBytes = new byte[4];
                in.read(cardIdBytes);
                hash.update(cardIdBytes);
                int cardId = ByteBuffer.wrap(cardIdBytes).getInt();
                byte [] answerLengthBytes = new byte[4];
                in.read(answerLengthBytes);
                hash.update(answerLengthBytes);
                int answerLength = ByteBuffer.wrap(answerLengthBytes).getInt();
                byte [] answerBytes = new byte[answerLength];
                in.read(answerBytes);
                hash.update(answerBytes);
                String answer = new String(answerBytes);
                byte [] videoLengthBytes = new byte[4];
                in.read(videoLengthBytes);
                hash.update(videoLengthBytes);
                int videoLength = ByteBuffer.wrap(videoLengthBytes).getInt();
                byte [] videoBytes = new byte[videoLength];
                in.read(videoBytes);
                hash.update(videoBytes);
                // create video file and card object, add to SharePackage cards
                Video video = vm.decodeVideo(videoBytes);
                Card card = cm.buildCard(video, answer);
                cards.add(card);
                idToCardMap.put(cardId, card);
            }
            // decode number of decks
            byte [] numDeckBytes = new byte[4];
            in.read(numDeckBytes);
            hash.update(numDeckBytes);
            int numDecks = ByteBuffer.wrap(numDeckBytes).getInt();
            List<Card> cardsInDeck;
            // decode decks
            for(int i = 0; i < numDecks; i++){
                byte [] deckIdBytes = new byte[4];
                in.read(deckIdBytes);
                hash.update(deckIdBytes);
                int deckId = ByteBuffer.wrap(deckIdBytes).getInt();
                byte [] nameLengthBytes = new byte[4];
                in.read(nameLengthBytes);
                hash.update(nameLengthBytes);
                int nameLength = ByteBuffer.wrap(nameLengthBytes).getInt();
                byte [] nameBytes = new byte[nameLength];
                in.read(nameBytes);
                hash.update(nameBytes);
                String name = new String(nameBytes);
                byte [] numCardsInDeckBytes = new byte[4];
                in.read(numCardsInDeckBytes);
                hash.update(numCardsInDeckBytes);
                int numCardsInDeck = ByteBuffer.wrap(numCardsInDeckBytes).getInt();
                cardsInDeck = new ArrayList<Card>();
                for(int c = 0; c < numCardsInDeck; c++){
                    byte [] cIdBytes = new byte[4];
                    in.read(cIdBytes);
                    hash.update(cIdBytes);
                    int cId = ByteBuffer.wrap(cIdBytes).getInt();
                    Card existingCard = idToCardMap.get(cId);
                    if(existingCard != null){
                        cardsInDeck.add(existingCard);
                    }
                }
                // create deck object, add to SharePackage decks
                Deck deck = dm.buildDeck(name, cardsInDeck);
                decks.add(deck);
            }
            sharePackage = new SharePackage(cards, decks);
        } catch (IOException e){
            e.printStackTrace();
        } catch (ObjectAlreadyExistsException e) {
            e.printStackTrace();
        }
        return sharePackage;
    }
}
