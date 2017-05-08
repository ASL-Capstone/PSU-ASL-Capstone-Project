package com.psu.capstonew17.pdxaslapp;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;


public class EditVideoActivity extends BaseActivity implements View.OnClickListener{
    private Uri videoUri;
    private VideoView videoView;

    //newly added to 'EditVideoActivity'
    private static SeekBar seekBar; //tracks current video's progress

    //Vars to pass to backend
    private boolean deleteAfter;
    private int startTime;
    private int endTime;
    private int quality;
    //private Rect cropRegion; //STRETCH GOAL!!!



    //Private inner-class used to update the seekBar
    private final Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if(seekBar != null) {
                seekBar.setProgress(videoView.getCurrentPosition());
            }

            if(videoView.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        videoView = (VideoView) findViewById(R.id.videoView_edit_video); //connect to video view in EditVideo layout

        Intent intent = getIntent(); //get Intent passed from 'CreateCardActivity'
        videoUri = intent.getData(); //get video URI data passed via an Intent from 'CreateCardActivity'

        if(videoUri == null) {
            Toast.makeText(this, "From EditCard: No URI Passed", Toast.LENGTH_SHORT).show(); //pop-up indicating No video passed from 'CreateCardActivity'
        }
        else {
            //connect seekBar to xml
            seekBar = (SeekBar) findViewById(R.id.seekBarEditVideo);
            //seekBar.setMax(videoView.getDuration()); //set 'seekBar's max limit to videos's length

            //set up video view to initialize 'seekBar' when the video is loaded
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    seekBar.setMax(videoView.getDuration()); //NEEDS TESTING!
                    seekBar.postDelayed(onEverySecond, 1000); //1000 milli-second delay
                }
            });

            videoView.setVideoURI(videoUri); //set view to locate current video needing to be edited
            //calls setOnPreparedListener??
            //videoView.start(); //start AFTER setting up the seek bar



        } //end of 'else'


    }


    @Override
    public void onClick(View view) {

    }
}
