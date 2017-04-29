package com.psu.capstonew17.backend.api.stubs;

import com.psu.capstonew17.backend.api.SharingTransmitListener;

/**
 * Created by Tim on 4/27/2017.
 */

public class SharingTransmitListenerStub implements SharingTransmitListener {
    public void onClientConnect(String peerID) {
        System.out.println("Sharing server connection on ID " + peerID + " complete.");
    }

    public void onTransmittedSuccessfully(String peerID) {
        System.out.println("Card(s) shared successfully to " + peerID);
    }

    public void onClientError(String peerID, DisconnectReason why) {
        System.out.println("Early connection termination with " + peerID + "due to" /*Reason why*/);
    }
}
