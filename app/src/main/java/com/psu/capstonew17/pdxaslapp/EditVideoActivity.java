package com.psu.capstonew17.pdxaslapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class EditVideoActivity extends AppCompatActivity {
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        videoView = (VideoView) findViewById(R.id.videoView_edit_video);

        // To get files from any resource folder (eg: raw, drawable, etc.)
        // Use the resource id
        int rawId = getResources().getIdentifier("josh",  "raw", getPackageName());

        // URI formation
        String path = "android.resource://" + getPackageName() + "/" + rawId;

        // Set the URI to play video file
        videoView.setVideoURI(Uri.parse(path));
    }

}
