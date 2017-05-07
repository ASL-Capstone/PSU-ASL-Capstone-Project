package com.psu.capstonew17.pdxaslapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;
import android.net.Uri;


public class EditVideoActivity extends BaseActivity implements View.OnClickListener{
    private Uri videoUri;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

//        videoView = (VideoView) findViewById(R.id.videoView2);

//        videoUri =  getIntent().getData();
//        if(videoUri == null)
//            Toast.makeText(this, "No URI Passed", Toast.LENGTH_SHORT);
//        else {
//            videoView.setVideoURI(videoUri);
//            videoView.start();
//        }

        videoView = (VideoView) findViewById(R.id.videoView_edit_video);



    }


    @Override
    public void onClick(View view) {

    }
}
