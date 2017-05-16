package com.psu.capstonew17.backend.sharing;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.google.zxing.BarcodeFormat;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.SharingReceiveListener;
import com.psu.capstonew17.backend.api.SharingTransmitListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by noahz on 4/28/17.
 */
public class SharingManager implements com.psu.capstonew17.backend.api.SharingManager {
    private static SharingManager instance = null;
    private List <WifiP2pDevice> listOfPeers = new ArrayList<WifiP2pDevice>();         //host list of peers from PeerListListener
    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifiChannel;
    private BroadcastReceiver wifiReceiver;

    //find peers to connect with
    private WifiP2pManager.PeerListListener peerListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            listOfPeers.clear();    //empty peer list
            listOfPeers.addAll(wifiP2pDeviceList.getDeviceList());  //repopulate with new devices
            if(listOfPeers.size() ==0){System.out.println("No devices within range");}  //need to modify for android device
        }
    };

    public static class LinkParameters {
        String ssid;
        String keySource;
        String netPassword;
    }

    public static com.psu.capstonew17.backend.api.SharingManager getInstance() {
        if(instance == null)
            instance = new SharingManager();
        return instance;
    }

    private SharingManager() {

    }

    /**
     * Generate a random string of given length, where each character is within the printable ASCII
     * range. String is guaranteed not to include spaces.
     */
    private String randomString(Random r, int length) {
        char arr[] = new char[length];
        for(int i = 0;i < length;i++) {
            // NOTE: 0x7e = '~', 0x21 = '!'
            // This limits the number to the printable ASCII range
            arr[i] = (char)r.nextInt(0x7e - 0x21);
        }

        return new String(arr);
    }

    @Override
    public Bitmap transmit(List<Card> cards, List<Deck> decks, TxOptions opts, SharingTransmitListener listener) {
        int timeout = opts.timeout;
        int maxTargets = opts.maxTargets;

        // generate connection parameters
        Random rand = new Random();
        LinkParameters param = new LinkParameters();
        param.ssid = randomString(rand, 16);
        param.keySource = randomString(rand, 32);
        param.netPassword = randomString(rand, 32);

        // generate QR code
        // TODO: figure out ZXing
        String ParamTotal = param.ssid + param.keySource + param.netPassword;   //concat param strings
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bmat = null;
        try{
            bmat = writer.encode(ParamTotal,BarcodeFormat.QR_CODE,100,100);    //create QR code, store in BitMatrx

        }catch (WriterException exception){exception.printStackTrace();}

        int height = bmat.getHeight();    //convert BitMatrix to bitmap
        int width = bmat.getWidth();
        int [] pix = new int[width * height];
        int offst =0;
        for(int Yaxis =0;Yaxis< height; ++Yaxis){
            offst = Yaxis * width;
            for(int Xaxis =0;Xaxis < width; ++ Xaxis){
                if(bmat.get(Xaxis,Yaxis)){pix[Xaxis + offst] = 0xFF000000;}     //if true, set to Black
                else{pix[Xaxis + offst] = 0xFFFFFFFF;}                          //else set to white
            }
        }

        Bitmap bmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
        bmap.setPixels(pix, 0, width, 0,0,width, height);
        return bmap;
    }

    public void boradcastReceive(){
        //TODO: implement broadcast reciever for client side
    }

    @Override
    public void receive(String code, RxOptions opts, SharingReceiveListener listener) {
        WifiP2pDevice aDevice = listOfPeers.get(0); //get device in front of list
        WifiP2pConfig configuration = new WifiP2pConfig();
        configuration.deviceAddress = aDevice.deviceAddress;    //MAC address IDing device
        configuration.wps.setup= WpsInfo.PBC;           //wifi protected setup push button config
        wifiManager.connect(wifiChannel, configuration, new WifiP2pManager.ActionListener() {
            //will using our own callback routine insted of WifiP2pManager.ActionListener()
            @Override
            public void onSuccess() {
                //if/else cases in broadcast reciver will provide noitificaiton for successful connect
            }

            @Override
            public void onFailure(int i) {
                System.out.println("Failed to connect");
            }
        });
    }
}
