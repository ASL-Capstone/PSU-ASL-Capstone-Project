package com.psu.capstonew17.backend.sharing;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadFactory;

public class SharingServer extends Service {
    private byte [] key; //keySource from SharingServer, used in creation of QR code
    private SharePackage toShare;
    private final IBinder mBinder = new Binder();

    SharingServer(byte [] key, SharePackage toShare){
        this.key = key;
        this.toShare = toShare;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private class ServerAsync extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
           try {
               ServerSocket serverSocket = new ServerSocket(8080);
               Socket client = serverSocket.accept();
               handleClient(client,key,toShare);
           }catch (IOException e){}
            return null;
        }
    }
    private void handleClient(Socket sock, byte[] key, SharePackage pkg) {
        Thread t = new Thread(new ServerSession(sock, key, pkg));
        t.start();
    }
}