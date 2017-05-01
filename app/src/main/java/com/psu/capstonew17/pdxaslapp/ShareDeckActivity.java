package com.psu.capstonew17.pdxaslapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.SharingManager;
import com.psu.capstonew17.backend.api.SharingTransmitListener;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

import java.util.ArrayList;
import java.util.List;

public class ShareDeckActivity extends BaseActivity implements View.OnClickListener {
    private Button submit;
    private LinearLayout ll;
    private RadioGroup rg;
    private List<Deck> decks;
    private Bitmap qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        this.ll = new LinearLayout(this);
        this.rg = new RadioGroup(this);
        this.ll.setOrientation(LinearLayout.VERTICAL);

        //TODO Database call should go here
        this.decks = TestingStubs.manyDecks();
        //List<String> deckLabels = new ArrayList<String>();

        //Create Radio Buttons For Each Deck and add them to the RadioGroup
        int i = 0;
        for(Deck deck:this.decks){
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(i);
            rdbtn.setText(deck.getName());
            this.rg.addView(rdbtn);
            ++i;
        }
        ((ViewGroup)findViewById(R.id.deckList)).addView(this.rg);
        this.submit = (Button) findViewById(R.id.button3);
        this.submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button3:
                int id = this.rg.getCheckedRadioButtonId();
                if(id != -1){
                    Deck toShare = decks.get(id);
                    //TODO create popup window for displaying the QR code recieved from backend
                    SharingTransmitListener listener;
                    List<Deck> sharing = new ArrayList<>();
                    sharing.add(toShare);
                    //SharingManager sharer;
                    //SharingManager.TxOptions ops = new SharingManager.TxOptions();
                    //TODO Find out how these params are decided (SharingManager.TxOptions timeout, maxTargets)
                    // Do they get default values? Do we ask the user?
                    //ops.timeout = 360;
                    //ops.maxTargets = 30;
                    //qrCode = sharer.transmit(null, toShare,ops, listener);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Deck Selected", Toast.LENGTH_SHORT).show();
                    //((TextView) findViewById(R.id.TESTTEXT)).setText("No Deck Selected");
                }

                break;

        }

    }
}
