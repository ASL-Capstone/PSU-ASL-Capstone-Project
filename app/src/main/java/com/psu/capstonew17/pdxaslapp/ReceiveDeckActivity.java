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

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveDeckActivity extends BaseActivity implements View.OnClickListener {
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
            } else {
                textView.setText("Scan Successful");
                // TODO hook up with backend, pass scanResu

            }

        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.buttonScanQR:
                qrScan.initiateScan();
                break;
        }

    }
}
