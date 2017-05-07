package com.psu.capstonew17.backend.api;
/**
 * Listener interface for sharing events.
 */
public class ExternalSharingTransmitListener implements SharingTransmitListener {
    enum DisconnectReason {
        AUTH_FAILURE,
        CHECKSUM_ERROR,
        TIMEOUT
    }

    /**
     * Called when a client connects to the sharing server.
     */
    @Override
    public void onClientConnect(String peerID) {

    }

    /**
     * Called when a client receives the shared object successfully.
     */
    @Override
    public void onTransmittedSuccessfully(String peerID) {

    }

    @Override
    public void onClientError(String peerID, SharingTransmitListener.DisconnectReason why) {

    }
}
