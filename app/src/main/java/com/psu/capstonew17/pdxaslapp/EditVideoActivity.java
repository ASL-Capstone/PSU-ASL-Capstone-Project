package com.psu.capstonew17.pdxaslapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.VideoManager;
import com.psu.capstonew17.backend.data.ExternalVideoManager;


public class EditVideoActivity extends BaseActivity implements View.OnClickListener{
    private Uri videoUri;
    private VideoView videoView;

    //newly added to 'EditVideoActivity'
    private SeekBar seekBar; //tracks current video's progress
    private Switch start_stop_switch; // off = modify stop time; on = modify start time
    private boolean endTimeSwitch; // set to value of above switch
    private Button submitButton; //attached to submit button (sends modifications to backend)
    private TextView startTimeText; //displays current starting point of video
    private TextView endTimeText; //displays current ending point of video
    private int currentProgress; //current progress of user-initiated seekBar movement

    //Vars to pass to backend //may replace with local vars at some point
    VideoManager.ImportOptions importOptions;
    //private boolean deleteAfter;
    //private int startTime;
    //private int endTime;
    //private int quality;
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

        //NOTE - each layout init block will be converted to a separate local method during 'polish'

        /**Set up start_stop switch
         *  - connect to button
         *  - set local variable 'endTimeSwitch' when switch is changed by user
         */
        start_stop_switch = (Switch) findViewById(R.id.start_stop_switch);
        start_stop_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean fromUser) {
                if(fromUser) {
                    endTimeSwitch = start_stop_switch.getShowText(); //set endTime to false if start selected; true if stop
                }
            }
        });


        /**Set up the submit button
         *  - connect to button
         *  - have button execute call to backend
         *
         */
        submitButton = (Button) findViewById(R.id.submitButtonEditCard);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitEdits(view);
            }
        });


        /**Set up seekBar connectivity
         *  - connect to videoVidew
         *  - set up progress change to connect to startTime/endTime
         */
        //MOVE SEEK BAR SET-UP TO SEPARATE METHOD
        //connect seekBar to xml
        seekBar = (SeekBar) findViewById(R.id.seekBarEditVideo);
        //seekBar.setMax(videoView.getDuration()); //set 'seekBar's max limit to videos's length
        //connect seekBar functionality to video --> video should respond to seekBar movement with these
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) { //if seekBar movement is user-initiated, adjust video accordingly
                    videoView.seekTo(progress);
                    currentProgress = progress; //NEED TO CHECK >> MAY NEED TO CONVERT progress TO milliseconds
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TO DO
                Toast.makeText(getApplicationContext(), "Starting to seek", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TO DO
                if(endTimeSwitch) {
                    Toast.makeText(getApplicationContext(), "Setting stop time crop", Toast.LENGTH_SHORT).show();
                    endTimeText.setText(R.string.stop_time_label + currentProgress);
                    importOptions.endTime = currentProgress;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Setting start time crop", Toast.LENGTH_SHORT).show();
                    startTimeText.setText(R.string.start_time_label + currentProgress);
                    importOptions.startTime = currentProgress;
                }

            }
        });


        //initialize import-options structure
        importOptions = new VideoManager.ImportOptions();
        //These members will eventually be set by EditVideo layout
        importOptions.cropRegion = null;
        //importOptions.deleteAfter = false;
        importOptions.startTime = 0;
        importOptions.quality = 10; //default quality


        //GET Intent passed from CreateACard Activity
        Intent intent = getIntent(); //get Intent passed from 'CreateCardActivity'
        videoUri = intent.getData(); //get video URI data passed via an Intent from 'CreateCardActivity'

        if(videoUri == null) {
            importOptions.endTime = 0;
            Toast.makeText(this, "From EditCard: No URI Passed", Toast.LENGTH_SHORT).show(); //pop-up indicating No video passed from 'CreateCardActivity'
        }
        else {
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
            videoView.start(); //start AFTER setting up the seek bar

            //set DEFAULT max video length to original video length
            importOptions.endTime = videoView.getDuration();
        } //end of 'else' //videoUri != null case



        /**Set up start and end time textViews
         *  - initialize to some default value
         *  - adjust when seekBar changed
         */
        startTimeText = (TextView) findViewById(R.id.textViewStartTimeLabel);
        startTimeText.setText(R.string.start_time_default);
        endTimeText = (TextView) findViewById(R.id.textViewStopTimeLabel);
        endTimeText.setText(R.string.stop_time_default + videoView.getDuration());

        /*
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
        */

    }


    /**
     * Check is user hits the 'Submit' Button
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(R.id.submitButtonEditCard == view.getId()){

            //UNcomment once backend is working + connected
            //submitEdits(view);

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
    }







    /**
     * Called when user hits the submit button
     * @param view
     */
    public void submitEdits(View view) {

        //OUTLINE TO CONNECT TO BACKEND
        VideoManager videoEditor = ExternalVideoManager.getInstance(this);

        //NEED TO: determine how to get video file path to pass to backend
        videoEditor.importVideo(getApplicationContext(), videoUri, importOptions, new VideoManager.VideoImportListener() {
            @Override
            public void onProgressUpdate(int current, int max) {
                //TO DO: indicate video loading progress bar
                finish();
            }

            @Override
            public void onComplete(Video vid) {
                //TO DO: once edited, send this video back to 'CreateCardActivity'
                finish();
            }

            @Override
            public void onFailed(Throwable err) {
                //TO DO: indicate "Failure" to calling routine
                finish();
            }
        });

    }






}
