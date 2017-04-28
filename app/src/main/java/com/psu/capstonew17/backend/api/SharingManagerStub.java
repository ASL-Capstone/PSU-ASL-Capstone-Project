package com.psu.capstonew17.backend.api;

import android.graphics.Bitmap;
import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class SharingManagerStub implements SharingManager {
    @Override
    public Bitmap transmit(List<Card> cards, List<Deck> decks, TxOptions opts, SharingTransmitListener listener) {
        return null;
    }

    public void receive(RxOptions opts, SharingReceiveListener listener) {
        return;
    }
}
