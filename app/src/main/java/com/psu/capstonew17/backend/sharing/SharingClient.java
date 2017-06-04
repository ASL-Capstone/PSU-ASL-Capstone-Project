package com.psu.capstonew17.backend.sharing;
import java.lang.String;
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
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import static android.os.Looper.getMainLooper;

/**
 * Created by RuddJohnson on 6/1/17.
 */

public class SharingClient extends Service {
    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifiChannel;
    private BroadcastReceiver wifiReceiver;
    private IntentFilter aIntentFilter;
    private final IBinder aBinder = new Binder();
    private Broadcast bReciever;

    @Override
    public void onCreate(){
        // create the broadcast receiver to manage P2P state
        aIntentFilter = new IntentFilter();
        aIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        aIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        aIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        aIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        wifiManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        wifiChannel = wifiManager.initialize(this, getMainLooper(), null);
      }

    /*
    Called from SharingManager, attempt to connect to device peer list, if successful, throw asynchronous thread to manage data transmission
    */
    public void initiateConnect(List<Card> cards, final List<Deck> decks) {
        WifiP2pDevice aDevice = bReciever.getPeer();//get device in front of list
        if(aDevice == null)         //no peers
        {return;}

        final WifiP2pConfig configuration = new WifiP2pConfig();
        configuration.deviceAddress = aDevice.deviceAddress;        //MAC address IDing device
        configuration.wps.setup = WpsInfo.PBC;                      //wifi protected setup push button config
        wifiManager.connect(wifiChannel, configuration, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {   //if successful, throw asynchronous thread to connect to server
                int port = 8080;
                try {
                    Socket socket = new Socket();                //create unconnected socket
                    socket.connect(new InetSocketAddress(configuration.deviceAddress, port), 10000);  //connect socket to server with ip address (MAC address identified above), port 8080, and timeout value in ms
                    OutputStream outStream = socket.getOutputStream();
                    ObjectOutputStream objOut = new ObjectOutputStream(outStream);                  //graph java object to output stream
                    objOut.writeObject(decks);                                                      //write deck to output stream
                    objOut.close();                                                                 //close streams to release resources
                    outStream.close();
                } catch (FileNotFoundException exc) {
                    exc.printStackTrace();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }   //exception handling
            }
            @Override
            public void onFailure(int i) {
                //handle
            }

        });
    }
    @Override
    public IBinder onBind(Intent intent) {
        return aBinder;
    }
}
