//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.psu.capstonew17.backend.api.SharingReceiveListener;
import com.psu.capstonew17.backend.sharing.SharingManager;

public class ReceiveDeckActivity extends BaseActivity implements View.OnClickListener {
    // layout views
    private Button bttScanQR;
    private TextView textView;

    // qr object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_deck);

        // view objects
        bttScanQR   = (Button) findViewById(R.id.buttonScanQR);
        textView    = (TextView) findViewById(R.id.textView_scanQR);

        bttScanQR.setOnClickListener(this);

        qrScan = new IntentIntegrator(this);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
            // if qrcode has nothing in it
            if (scanResult.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
            // get qr code, handle receive dekc
            } else {
                textView.setText("Scan Successful");

                SharingManager sharingManager = (SharingManager) SharingManager.getInstance();

                com.psu.capstonew17.backend.api.SharingManager.RxOptions rx
                        = new com.psu.capstonew17.backend.api.SharingManager.RxOptions();
                rx.retries = 3;

                sharingManager.receive(scanResult.getContents(), rx, new SharingReceiveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ReceiveDeckActivity.this, "Successful Receive New Decks",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ErrorType type) {
                        Toast.makeText(ReceiveDeckActivity.this, "Error: Failed to receive deck",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            // start scanning process
            case R.id.buttonScanQR:
                qrScan.initiateScan();
                break;
        }

    }
}
