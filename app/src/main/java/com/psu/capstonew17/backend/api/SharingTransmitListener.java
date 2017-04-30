//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

/**
 * Listener interface for sharing events.
 */
public interface SharingTransmitListener {
    enum DisconnectReason {
        AUTH_FAILURE,
        CHECKSUM_ERROR,
        TIMEOUT
    }

    /**
     * Called when a client connects to the sharing server.
     */
    void onClientConnect(String peerID);

    /**
     * Called when a client receives the shared object successfully.
     */
    void onTransmittedSuccessfully(String peerID);

    /**
     * Called when a connected client disconnects before receiving all data.
     */
    void onClientError(String peerID, DisconnectReason why);
}
