package com.psu.capstonew17.pdxaslapp;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;
import android.net.Uri;


public class EditVideoActivity extends BaseActivity implements View.OnClickListener{
    private Uri videoUri;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card);

        videoView = (VideoView) findViewById(R.id.videoView);

        videoUri =  getIntent().getData();
        if(videoUri == null)
            Toast.makeText(this, "No URI Passed", Toast.LENGTH_SHORT);
        else {
            videoView.setVideoURI(videoUri);
            videoView.start();
        }

    }


    @Override
    public void onClick(View view) {

    }
}
