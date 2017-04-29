package com.psu.capstonew17.backend.api;
/**
 * Listener interface to handle sharing receive operations
 */
public class  ExternalSharingReceiveListener implements SharingReceiveListener {
    enum ErrorType {
        CONNECTION_FAILED
    }

    /**
     * Notify the handler that the share operation was successful
     */
    @Override
    void onSuccess();

    /**
     * Notify the handler that the share operation failed.
     */
    @Override
    void onError(ErrorType type);
}