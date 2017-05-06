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
        switch(why) {
            case AUTH_FAILURE:
                System.out.println("Early connection termination with " + peerID + " Authentication Failue");
                break;
            case CHECKSUM_ERROR:
                System.out.println("Early connection termination with " + peerID + " Checksum Error ");
                break;
            case TIMEOUT:
                System.out.println("Early connection termination with " + peerID + " Timeout");
                break;
        }

    }
}
