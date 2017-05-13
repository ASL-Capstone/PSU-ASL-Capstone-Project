package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.SharingTransmitListener;
import com.psu.capstonew17.backend.api.stubs.SharingTransmitListenerStub;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.sharing.SharingManager;

import java.util.List;


public class DispQRCodeActivity extends BaseActivity implements View.OnClickListener {
    private Button exit; //The exit to home button
    private Bitmap qrCode; //Holds the qrCode bitmap
    private TextView downloads; //The display for how many downloads there have been
    private int downloadCount; //Counter for how many downloads there have been
    private ImageView imageView; //View for the bitmap
    private TextView textDeckName; //Displays the name of the deck being shared

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        //Initialize Members and Atatch to Layout
        downloadCount = 0;
        exit = (Button) findViewById(R.id.button_QR_code_finish);
        exit.setOnClickListener(this);
        textDeckName = (TextView) findViewById(R.id.TextDeckName);
        String deckName  = getIntent().getStringExtra("DECKNAME");
        textDeckName.setText("Sharing Deck " + deckName);
        imageView = (ImageView) findViewById(R.id.qrCodeView);
        downloads = (TextView) findViewById(R.id.textSharedCount);
        //Set the Downloads Display to show number of downloads
        String downloadDisplay = String.valueOf(downloadCount) + getString(R.string.shared_counter);
        downloads.setText(downloadDisplay);

        DeckManager deckManager = ExternalDeckManager.getInstance(this);


        List<Deck> decks = deckManager.getDecks(deckName);
 
        SharingTransmitListener listener = new SharingTransmitListener(){
            @Override
            public void onClientConnect(String peerID) {

            }

            @Override
            public void onTransmittedSuccessfully(String peerID) {
                downloadCount += 1;
                String downloadDisplay = String.valueOf(downloadCount) + getString(R.string.shared_counter);
                downloads.setText(downloadDisplay);
            }

            @Override
            public void onClientError(String peerID, DisconnectReason why) {
                switch(why){
                    case AUTH_FAILURE:
                        Toast.makeText(getApplicationContext(), "Authorization Failed for Sharing", Toast.LENGTH_SHORT);
                        break;
                    case CHECKSUM_ERROR: //TODO Find a correct output for this error
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                        break;
                    case TIMEOUT:
                        Toast.makeText(getApplicationContext(), "Sharing Timed Out", Toast.LENGTH_SHORT);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                }
            }
        };

        com.psu.capstonew17.backend.api.SharingManager sharer = SharingManager.getInstance();
        SharingManager.TxOptions ops = new SharingManager.TxOptions();
        ops.timeout = 360;
        ops.maxTargets = 30;
        qrCode = sharer.transmit(null, decks,ops, listener);


        if(qrCode != null) {
            imageView.setImageBitmap(qrCode);
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_QR_code_finish:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                return;
            default:
                break;

        }
    }
}
