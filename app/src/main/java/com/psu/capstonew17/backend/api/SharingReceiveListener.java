//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

/**
 * Listener interface to handle sharing receive operations
 */
public interface SharingReceiveListener {
    enum ErrorType {
        CONNECTION_FAILED
    }

    /**
     * Notify the handler that the share operation was successful
     */
    void onSuccess();

    /**
     * Notify the handler that the share operation failed.
     */
    void onError(ErrorType type);
}
