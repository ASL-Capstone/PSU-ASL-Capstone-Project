package com.psu.capstonew17.backend.api.stubs;

import android.graphics.Bitmap;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.SharingManager;
import com.psu.capstonew17.backend.api.SharingReceiveListener;
import com.psu.capstonew17.backend.api.SharingTransmitListener;

import java.util.List;

/**
 * Created by Tim on 4/27/2017.
 */

public class SharingManagerStub implements SharingManager {
    @Override
    public Bitmap transmit(List<Card> cards, List<Deck> decks, TxOptions opts, SharingTransmitListener listener) {
        return null;
    }

    public void receive(String code, RxOptions opts, SharingReceiveListener listener) {
        return;
    }
}
