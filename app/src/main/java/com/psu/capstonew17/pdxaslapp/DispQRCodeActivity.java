package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;

import java.util.List;


public class DispQRCodeActivity extends BaseActivity implements View.OnClickListener {
    private Button exit;
    private Bitmap qrCode;
    private TextView downloads; //The display for how many downloads there have been
    private int downloadCount;
    private ImageView imageView;
    private TextView textDeckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        downloadCount = 0;

        DeckManager deckManager = ExternalDeckManager.getInstance(this);
        //TODO Make extra name constant
        String deckName  = getIntent().getStringExtra("DECKNAME");

        textDeckName = (TextView) findViewById(R.id.TextDeckName);
        textDeckName.setText(deckName);
        imageView = (ImageView) findViewById(R.id.qrCodeView);

        List<Deck> decks = deckManager.getDecks(deckName);
        //Get the bitmap of the QR Code
        //TODO Override method for what happens on a successful share
        //Should update download counter and the downloads textview

        /*
        SharingTransmitListener listener = new ExternalSharingTransmitListener();
        com.psu.capstonew17.backend.api.SharingManager sharer = SharingManager.getInstance();
        SharingManager.TxOptions ops = new SharingManager.TxOptions();
        //TODO Find out how these params are decided (SharingManager.TxOptions timeout, maxTargets)
        //Do they get default values? Do we ask the user?
        ops.timeout = 360;
        ops.maxTargets = 30;
        qrCode = sharer.transmit(null, decks,ops, listener);
        */

        if(qrCode != null) {
            imageView.setImageBitmap(qrCode);
        }

        exit = (Button) findViewById(R.id.button_QR_code_finish);
        exit.setOnClickListener(this);

        downloads = (TextView) findViewById(R.id.textSharedCount);

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
