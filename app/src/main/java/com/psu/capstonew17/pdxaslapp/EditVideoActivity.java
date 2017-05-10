package com.psu.capstonew17.pdxaslapp;


import android.app.Activity;
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

    //Vars to pass to backend //may replace with local vars at some point
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

            //connect seekBar functionality to video --> video should respond to seekBar movement with these
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) { //if seekBar movement is user-initiated, adjust video accordingly
                        videoView.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //TO DO
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //TO DO
                }
            });

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


            //OUTLINE TO CONNECT TO BACKEND
            /*
            VideoManager videoManager = ExternalVideoManager.getInstance(this);
            VideoManager.ImportOptions importOptions = new VideoManager.ImportOptions();

            //These members will eventually be set by EditVideo layout
            importOptions.cropRegion = null;
            importOptions.deleteAfter = false;
            importOptions.endTime = videoView.getDuration();
            importOptions.startTime = 0;
            importOptions.quality = 10; //default degredation

            videoManager.importVideo(new File(videoUri.getPath()), importOptions, new VideoManager.VideoImportListener() {
                @Override
                public void onProgressUpdate(int current, int max) {
                    //TO DO: indicate video loading progress bar
                }

                @Override
                public void onComplete(Video vid) {
                    //TO DO: once edited, send this video back to 'CreateCardActivity'
                }

                @Override
                public void onFailed(Throwable err) {
                    //TO DO: indicate "Failure" to calling routine
                }
            });
            */

        } //end of 'else' //videoUri != null case


        //FOR NOW, just return the un-edited video back to 'CreateCard'
        //Will change once connected to backend, and time-cropping is functional
        Intent returnIntent = new Intent();
        if(videoUri != null) {
            returnIntent.setData(videoUri);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else {
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }



    }


    @Override
    public void onClick(View view) {

    }
}
