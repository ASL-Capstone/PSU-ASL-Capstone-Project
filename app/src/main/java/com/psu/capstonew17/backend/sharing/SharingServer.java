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
import android.os.Binder;
import android.os.IBinder;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class SharingServer extends Service {
    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifiChannel;
    private BroadcastReceiver wifiReceiver;
    private List <WifiP2pDevice> listOfPeers = new ArrayList<WifiP2pDevice>();         //host list of peers from PeerListListener
    private final IBinder mBinder = new Binder();
    private Activity activity;

    @Override
    public IBinder onBind(Intent intent) {return mBinder;}
    //find peers to connect with
    private WifiP2pManager.PeerListListener peerListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            listOfPeers.clear();    //empty peer list
            listOfPeers.addAll(wifiP2pDeviceList.getDeviceList());  //repopulate with new devices
            if(listOfPeers.size() ==0){System.out.println("No devices within range");}  //need to modify for android device
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        wifiManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        wifiChannel = wifiManager.initialize(this, getMainLooper(), null);

        // create the broadcast receiver to manage P2P state
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

                wifiReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction(); //get current intent action
                        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))     //determine if statis of connection has changed
                        {
                    /*Todo:  FINISH CASE
                        //Check that Wifi p2p is enabled
                        int wifiState;  //to hold integer associated with currect wifi p2p state
                        wifiState = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1); //retrieve intent information
                        if(wifiState ==wifiManager.WIFI_P2P_STATE_ENABLED)
                        {

                        }
                    */
                        }
                        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))    //if the list of peers has changed
                        {
                            if(wifiManager != null){wifiManager.requestPeers(wifiChannel,peerListener);}        //if wifi manager has been instantiated, refresh list of peers
                            else{/*TODO: handle case where peers have not changed*/}
                        }
                        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
                        {
                            if(wifiManager == null){ return;}                               //if not instantiated return
                            NetworkInfo ntwkInfo = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                            //get connection info if we are connected
                            if (ntwkInfo.isConnected()) {wifiManager.requestConnectionInfo(wifiChannel, (WifiP2pManager.ConnectionInfoListener)activity);}
                            else {/*TODO: handle case where we aren't connected*/ }
                        }
                        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
                        {


                        }
                    }
                };
                registerReceiver(wifiReceiver, intentFilter);
            }


    /*
  Called from SharingManager, attempt to connect to device peer list, if successful, throw asynchronous thread to manage data transmission
*/
    public void initiateConnect(List<Card> cards, final List<Deck> decks) {
        WifiP2pDevice aDevice = listOfPeers.get(0); //get device in front of list
        final WifiP2pConfig configuration = new WifiP2pConfig();
        configuration.deviceAddress = aDevice.deviceAddress;    //MAC address IDing device
        configuration.wps.setup = WpsInfo.PBC;                    //wifi protected setup push button config
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
}