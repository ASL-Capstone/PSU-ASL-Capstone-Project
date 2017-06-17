package com.psu.capstonew17.backend.sharing;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RuddJohnson on 6/2/17.
 */

public class Broadcast extends BroadcastReceiver {
    private static String TAG = "Broadcast Receivert ";
    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifiChannel;
    private List<WifiP2pDevice> listOfPeers = new ArrayList<WifiP2pDevice>();         //host list of peers from PeerListListener
    

    private WifiP2pManager.PeerListListener peerListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            listOfPeers.clear();    //empty peer list
            listOfPeers.addAll(wifiP2pDeviceList.getDeviceList());  //repopulate with new devices
            if (listOfPeers.size() == 0) {
                Log.i(TAG,"No peers available");
                return;
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();                                     //get current intent action

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))        //determine if statis of connection has changed
        {
            //Check that Wifi p2p is enabled
            int wifiState;                                                      //to hold integer associated with currect wifi p2p state
            wifiState = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1); //retrieve intent information
            if (wifiState == wifiManager.WIFI_P2P_STATE_ENABLED) {
                Log.i(TAG,"Wifi enabled");
            }
            else{Log.e(TAG,"Wifi not enabled");return;}
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))    //if the list of peers has changed
        {
            if (wifiManager != null) {
                wifiManager.requestPeers(wifiChannel, peerListener);
            }                                                                        //if wifi manager has been instantiated, refresh list of peers
            else {Log.e(TAG,"Wifimanager not enabled");return;}

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (wifiManager == null) {return;}                                                                       //if not instantiated return
            NetworkInfo ntwkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                                                                                                                    //get connection info if we are connected
            if (ntwkInfo.isConnected()) {
                wifiManager.requestConnectionInfo(wifiChannel, (WifiP2pManager.ConnectionInfoListener) peerListener);
            } else {Log.e(TAG,"Connection to peer has changed");}
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice dev = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_STATE);
            Log.i(TAG,"wifi status of device changed to: "+dev.status);
        }
    }

    public WifiP2pDevice getPeer(){                             //pull a peer from the front fron of the peer list list
        if(listOfPeers.size() != 0)
        {return listOfPeers.get(0);}
        return null;
    }
}
