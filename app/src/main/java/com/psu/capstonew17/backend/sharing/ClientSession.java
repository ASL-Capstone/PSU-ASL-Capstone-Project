package com.psu.capstonew17.backend.sharing;

import java.net.Socket;

/**
 * Client-side sharing protocol session.
 *
 * Created by noahz on 4/28/17.
 */
class ClientSession {
    private Socket sck;
    private byte[] key;

    public ClientSession(Socket sock, byte[] key) {
        sck = sock;
        this.key = key;
    }
}
