//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.pdxaslapp.FrontEndTestStubs.TestingStubs;

import java.util.ArrayList;

public class CreateCardActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private ListRow row;
    private ArrayList<ListRow> list = new ArrayList<>();
    private ArrayList<Integer> selectedIndex;

    private CustomArrayAdapter myAdapter;
    private Uri videoUri;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_TAKE_GALLERY_VIDEO = 2;
    static final int REQUEST_EDIT_VIDEO = 3;

    static final int MIN_LABEL_LENGTH = 3;
    static final int MAX_LABEL_LENGTH = 250;

    private Button bttSubmit;
    private Button bttGetVideo;
    private Button bttRecordVideo;
    private Button bttUseVideo;
    private EditText editText;
    private VideoView videoView;

    private String videoLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        bttSubmit = (Button) findViewById(R.id.button_submit);
        bttSubmit.setText("Create");
        bttSubmit.setOnClickListener(this);

        bttGetVideo = (Button) findViewById(R.id.buttonFromGallery);
        bttGetVideo.setOnClickListener(this);

        bttRecordVideo = (Button) findViewById(R.id.buttonRecordVideo);
        bttRecordVideo.setOnClickListener(this);

        bttUseVideo = (Button) findViewById(R.id.buttonUseVIdeo);
        bttUseVideo.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.edit_text_video_answer);
        editText.setOnClickListener(this);

        videoView = (VideoView) findViewById (R.id.videoView);

        //ArrayList<Deck> decksList = new ArrayList<>(ExternalDeckManager.getInstance(this).getDecks(null));

        //get Decks from current testing
        ArrayList<Deck> deckList = new ArrayList<>(TestingStubs.manyDecks());

        for (int i = 0; i < deckList.size(); ++i) {
            ListRow listRow = new ListRow(deckList.get(i).getName() , false);
            list.add(listRow);
        }

        listView = (ListView) findViewById(R.id.list_items);
        myAdapter =  new CustomArrayAdapter(this, 0, list);
        listView.setAdapter(myAdapter);


        // hide views
        bttSubmit.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch(view.getId()) {
            case R.id.buttonRecordVideo:
                if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                    dispatchTakeVideoIntent();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Camera Not Available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonFromGallery:
                intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);

                break;
            case R.id.buttonUseVIdeo:
                //TODO Package video URI and call edit video intent

                if(videoUri != null){
                    intent = new Intent(this, EditVideoActivity.class);
                    intent.setData( videoUri);
//                    startActivity(intent);
                    startActivityForResult(intent, REQUEST_EDIT_VIDEO);
                    finish();
                }
                break;

            case R.id.button_submit:
                videoLabel = editText.getText().toString();

                if (videoLabelCheck() && videoFileCheck() && deckSelectedCheck()) {
                    // TODO create new card
                }

                break;


        }
    }

    private boolean videoLabelCheck() {

        videoLabel = editText.getText().toString();
        if (videoLabel.length() < MIN_LABEL_LENGTH || videoLabel.length() > MAX_LABEL_LENGTH) {
            Toast.makeText(this, "Label length should be in range ["
                    + MIN_LABEL_LENGTH + ", " + MAX_LABEL_LENGTH + "]", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean videoFileCheck() {
        if (videoUri == null) {
            Toast.makeText(this, "Missing video!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean deckSelectedCheck() {

        if (listView.getVisibility() == View.GONE) {
            listView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please select at least one deck", Toast.LENGTH_SHORT).show();
            return false;
        }

        // update list
        list = myAdapter.getItems();
        selectedIndex = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); ++i) {
            if (row.isChecked) {
                selectedIndex.add(i);
            }
        }

        if (selectedIndex == null || selectedIndex.size() < 1) {
            Toast.makeText(this, "Please select at least one deck", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent();
        takeVideoIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
        else{
            Toast.makeText(getApplicationContext(), "Record Video Failed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {

            case REQUEST_VIDEO_CAPTURE:
                if (resultCode == RESULT_OK) {
                    videoUri = intent.getData();
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(videoUri);
                    videoView.start();
                    bttSubmit.setVisibility(View.VISIBLE);
                }
                break;

            case REQUEST_EDIT_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    videoUri = intent.getData();
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(videoUri);
                    videoView.start();
                    bttSubmit.setVisibility(View.VISIBLE);
                }
                break;

        }


    }
}

