package com.psu.capstonew17.backend.data;

import com.psu.capstonew17.backend.api.SharingReceiveListener;

/**
 * Listener interface to handle sharing receive operations
 */
public class ExternalSharingReceiveListener implements SharingReceiveListener {
    enum ErrorType {
        CONNECTION_FAILED
    }

    /**
     * Notify the handler that the share operation was successful
     */
    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(SharingReceiveListener.ErrorType type) {

    }
}