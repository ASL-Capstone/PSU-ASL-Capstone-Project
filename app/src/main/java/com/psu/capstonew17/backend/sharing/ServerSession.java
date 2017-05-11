package com.psu.capstonew17.backend.sharing;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * Server-side sharing protocol session
 *
 * Created by noahz on 4/28/17.
 */

class ServerSession {
    private static final String TAG = "ServerSession";
    private Socket sck;
    private byte[] key;
    private SharePackage pack;

    public ServerSession(Socket sock, byte[] key, SharePackage pack) {
        sck = sock;
        this.key = key;
        this.pack = pack;
    }

    /**
     * Handle the given client connection
     */
    public void run() throws IOException {
        OutputStream out = sck.getOutputStream();
        InputStream in = sck.getInputStream();

        // generate challenge
        byte[] challenge_salt = Protocol.randomBytes(new Random(), 32);
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance("SHA2");
        } catch(NoSuchAlgorithmException e) {
            Log.wtf(TAG, "Cannot construct SHA2 hash", e);
            sck.close();
            return;
        }
        hash.update(challenge_salt);
        hash.update(key);
        byte[] response = hash.digest();

        if(response.length != 32) {
            Log.wtf(TAG, "Invalid SHA2 hash length");
            sck.close();
            return;
        }

        // send challenge and get response
        out.write(challenge_salt);

        byte[] client_response = new byte[32];
        int read = 0, nread;
        while((nread = in.read(client_response, read, 32-read)) >= 0);
        if(nread == -1 || !Arrays.equals(client_response, response)) {
            // protocol error
            sck.close();
            return;
        }

        // okay, handshake is good - send the data
        try {
            hash = MessageDigest.getInstance("SHA2");
        } catch(NoSuchAlgorithmException e) {
            Log.wtf(TAG, "Cannot construct SHA2 hash", e);
            sck.close();
            return;
        }
        pack.serializeTo(out, hash);
        byte[] computed_checksum = hash.digest();

        read = 0;
        while((nread = in.read(client_response, read, 32-read)) >= 0);
        if(nread == -1) {
            // protocol error
            sck.close();
            return;
        } else if(!Arrays.equals(client_response, computed_checksum)) {
            out.write(Protocol.MT_CKSUM_ERROR);
            // TODO: Retry on failure?
        }
        out.write(Protocol.MT_CKSUM_VALID);
        sck.close();
    }
}
