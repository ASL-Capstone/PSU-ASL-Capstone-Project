package com.psu.capstonew17.backend.api.stubs;

import com.psu.capstonew17.backend.api.SharingReceiveListener;

/**
 * Created by Tim on 4/27/2017.
 */

public class SharingReceiveListenerStub implements SharingReceiveListener {
    public void onSuccess() {
        System.out.println("Share successful");
    }
    public void onError(ErrorType type) {
        System.out.println("Connection failed");
    }
}
