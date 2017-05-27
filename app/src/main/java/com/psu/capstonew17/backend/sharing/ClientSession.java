package com.psu.capstonew17.backend.sharing;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Client-side sharing protocol session.
 *
 * Created by noahz on 4/28/17.
 */
class ClientSession {
    private static final String TAG = "ServerSession";
    private Socket sck;
    private byte[] key;
    private Context context;

    public ClientSession(Socket sock, byte[] key, Context context) {
        sck = sock;
        this.key = key;
        this.context = context;
    }

    /**
     * Handle server communication
     */
    public SharePackage run() throws IOException {
        OutputStream out = sck.getOutputStream();
        InputStream in = sck.getInputStream();

        // read challenge
        byte[] challenge = new byte[32];
        int nread, read = 0;
        while((nread = in.read(challenge, read, 32-read)) >= 0);
        if(nread == -1) {
            // unexpected socket error
            sck.close();
            return null;
        }

        // compute response
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance("SHA2");
        } catch(NoSuchAlgorithmException e) {
            Log.wtf(TAG, "Cannot construct SHA2 hash", e);
            sck.close();
            return null;
        }
        hash.update(challenge);
        hash.update(key);
        byte[] response = hash.digest();

        if(response.length != 32) {
            Log.wtf(TAG, "Invalid SHA2 hash length");
            sck.close();
            return null;
        }

        // send it
        out.write(response);

        // read response
        // TODO: Deserialization
        try {
            hash = MessageDigest.getInstance("SHA2");
        } catch(NoSuchAlgorithmException e) {
            Log.wtf(TAG, "Cannot construct SHA2 hash", e);
            sck.close();
            return null;
        }
        // Serialization format must be self-terminating. Hash must be updated with all content bytes.
        SharePackage pack = SharePackage.deserializeFrom(in, hash, context);

        // respond with checksum
        out.write(hash.digest());

        // read the confirmation/error message
        int msg = in.read();
        if(msg == Protocol.MT_CKSUM_ERROR) {
            // protocol error
            // TODO: retry?
            sck.close();
            return null;
        } else if(msg != Protocol.MT_CKSUM_VALID) {
            Log.wtf(TAG, "Unknown protocol message type");
            sck.close();
            return null;
        }

        // success
        sck.close();
        return pack;
    }
}
