//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class CreateCardActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private ListRow row;
    private ArrayList<ListRow> list = new ArrayList<>();
    private CustomArrayAdapter myAdapter;
    private Uri videoUri;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_TAKE_GALLERY_VIDEO=1;

    private Button bttSubmit;
    private Button bttGetVideo;
    private Button bttRecordVideo;

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);


        bttSubmit = (Button) findViewById(R.id.buttonUseVIdeo);
        bttSubmit.setOnClickListener(this);
        bttGetVideo = (Button) findViewById(R.id.buttonFromGallery);
        bttGetVideo.setOnClickListener(this);
        bttRecordVideo = (Button) findViewById(R.id.buttonRecordVideo);
        bttRecordVideo.setOnClickListener(this);

        videoView = (VideoView) findViewById(R.id.videoView);

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
                    startActivity(intent);
                    finish();
                }

                break;


        }
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
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
            bttSubmit.setVisibility(View.VISIBLE);
        }
    }
}

