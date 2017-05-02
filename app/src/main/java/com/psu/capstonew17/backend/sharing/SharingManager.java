package com.psu.capstonew17.backend.sharing;

import android.graphics.Bitmap;

import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.SharingReceiveListener;
import com.psu.capstonew17.backend.api.SharingTransmitListener;

import java.util.List;
import java.util.Random;

/**
 * Created by noahz on 4/28/17.
 */
public class SharingManager implements com.psu.capstonew17.backend.api.SharingManager {
    private static SharingManager instance = null;

    public static class LinkParameters {
        String ssid;
        String keySource;
        String netPassword;
    }

    public static com.psu.capstonew17.backend.api.SharingManager getInstance() {
        if(instance == null)
            instance = new SharingManager();
        return instance;
    }

    private SharingManager() {
    }

    /**
     * Generate a random string of given length, where each character is within the printable ASCII
     * range. String is guaranteed not to include spaces.
     */
    private String randomString(Random r, int length) {
        char arr[] = new char[length];
        for(int i = 0;i < length;i++) {
            // NOTE: 0x7e = '~', 0x21 = '!'
            // This limits the number to the printable ASCII range
            arr[i] = (char)r.nextInt(0x7e - 0x21);
        }

        return new String(arr);
    }

    @Override
    public Bitmap transmit(List<Card> cards, List<Deck> decks, TxOptions opts, SharingTransmitListener listener) {
        int timeout = opts.timeout;
        int maxTargets = opts.maxTargets;

        // generate connection parameters
        Random rand = new Random();
        LinkParameters param = new LinkParameters();
        param.ssid = randomString(rand, 16);
        param.keySource = randomString(rand, 32);
        param.netPassword = randomString(rand, 32);

        // generate QR code
        // TODO: figure out ZXing

        // start server

        return null;
    }

    @Override
    public void receive(RxOptions opts, SharingReceiveListener listener) {
    }
}
