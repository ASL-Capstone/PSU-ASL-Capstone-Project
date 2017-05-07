//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.SharingManager;
import com.psu.capstonew17.backend.api.SharingTransmitListener;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

import java.util.ArrayList;
import java.util.List;

public class ShareDeckActivity extends BaseActivity implements View.OnClickListener{
    private Button submit;
    private LinearLayout ll;
    private RadioGroup rg;
    private List<Deck> decks;
    private ScrollView sv;

    //Popup Window Objects
    private PopupWindow popupWindow;
    private LinearLayout popupLL;
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        this.ll = new LinearLayout(this);
        this.rg = new RadioGroup(this);
        this.ll.setOrientation(LinearLayout.VERTICAL);

        //TODO Database call should go here
        this.decks = TestingStubs.manyDecks();

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

        this.sv = (ScrollView) findViewById(R.id.decksView);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button3:
                int id = this.rg.getCheckedRadioButtonId();
                if(id != -1){
                    Deck toShare = decks.get(id);
                    //TODO create popup window for displaying the QR code recieved from backend
                    Intent intent = new Intent(this, DispQRCodeActivity.class);
                    //toShare.getName();
                    intent.putExtra("DECKNAME", toShare.getName());
                    startActivity(intent);
                    finish();
                    return;
                    /*
                    toShare.getName();
                    List<Deck> sharing = new ArrayList<>();
                    sharing.add(toShare);
                    setUpPopupWindow(sharing);
                    */
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Deck Selected", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    public void setUpPopupWindow(List<Deck> sharing){


        //sv.setVisibility(View.INVISIBLE);
        //submit.setVisibility(View.INVISIBLE);
/*        popupWindow = new PopupWindow(this);
        popupWindow.showAtLocation(ll, Gravity.BOTTOM, 10, 10);
        popupWindow.update(50,50,320,90);
        popupWindow.showAtLocation(ll, Gravity.BOTTOM, 10, 10);*/
        /*
        popupLL = new LinearLayout(this);
        popupLL.setOrientation(LinearLayout.VERTICAL);
        imageView = new ImageView(this);
        imageView.setImageBitmap(qrCode);
        popupLL.addView(imageView);
        popupWindow.setContentView(popupLL);
        */




    }
}
