package com.psu.capstonew17.backend.api;
/**
 * Listener interface for sharing events.
 */
public class ExternalSharingTransmitListener implements SharingTransmitListener{
    enum DisconnectReason {
        AUTH_FAILURE,
        CHECKSUM_ERROR,
        TIMEOUT
    }

    /**
     * Called when a client connects to the sharing server.
     */
    @Override
    void onClientConnect(String peerID);

    /**
     * Called when a client receives the shared object successfully.
     */
    @Override
    void onTransmittedSuccessfully(String peerID);

    /**
     * Called when a connected client disconnects before receiving all data.
     */
    @Override
    void onClientError(String peerID, DisconnectReason why);
}