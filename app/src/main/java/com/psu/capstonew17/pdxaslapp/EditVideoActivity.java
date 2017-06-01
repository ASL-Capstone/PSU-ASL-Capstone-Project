//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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
import com.psu.capstonew17.backend.api.VideoManager;


public class EditVideoActivity extends BaseActivity implements View.OnClickListener{
    public static final String EDITED_VIDEO = "video";
    public static final String ERROR = "ERROR_with_importing_options";
    public static final String START = "start";
    public static final String END = "end";
    public static final String QUALITY = "quality";
    private static final String CROP = "cropRegion";

    private Uri videoUri;
    private VideoView videoView;

    //UI
    private SeekBar seekBar; //tracks current video's progress
    private Switch start_stop_switch; // off = modify stop time; on = modify start time
    private Switch deleteFromGalSwitch; //switch used to indicate video should be removed from gallery
    private boolean deleteFromGal;
    private boolean endTimeSwitch; // set to value of above switch
    private Button submitButton; //attached to submit button (sends modifications to backend)
    private Button playPauseButton;
    private TextView startTimeText; //displays current starting point of video
    private TextView endTimeText; //displays current ending point of video
    private TextView seekPositionDisplay;

    //local helper variables
    private int currentProgress; //current progress of user-initiated seekBar movement
    private boolean firstAdjustment; //used to handle error message when using "start/stop" switch and seekBar

    //Vars to pass to backend //may replace with local vars at some point
    VideoManager.ImportOptions importOptions;

    //Private inner-class used to update the seekBar
    private final Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if(seekBar != null) {
                seekBar.setProgress(videoView.getCurrentPosition());
            }

            if(videoView.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 0); //int changes "pace" of seek ball
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        videoView = (VideoView) findViewById(R.id.videoView_edit_video); //connect to video view in EditVideo layout

        //NOTE - each layout init block will be converted to a separate local method during 'polish'

        //set up as first run-through
        firstAdjustment = true;

        /**Set up start_stop switch
         *  - connect to button
         *  - set local variable 'endTimeSwitch' when switch is changed by user
         */
        start_stop_switch = (Switch) findViewById(R.id.start_stop_switch);
        start_stop_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                endTimeSwitch = isChecked;
                if(endTimeSwitch) {
                    Toast.makeText(getApplicationContext(), "The seek bar will now update the STOP TIME", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "The seek bar will now update the START TIME", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**
         * Set up the "Delete From Gallery" swtich;
         * Determines if the video should be removed from the gallery following the edit
         */
        deleteFromGalSwitch = (Switch) findViewById(R.id.DeleteFromGallerySwitch);
        deleteFromGalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isNo) {
                deleteFromGal = (!isNo);
                if(deleteFromGal) {
                    Toast.makeText(getApplicationContext(), "The video will be removed from the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "The video will remain in the Gallery", Toast.LENGTH_SHORT).show();
                }
            }
        });




        /**Set up the submit button
         *  - connect to button
         *  - have button execute call to backend
         *
         */
        submitButton = (Button) findViewById(R.id.submitButtonEditCard);
        submitButton.setOnClickListener(this);

        /*submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UNCOMMENT once backend connected - submitEdits(view);
                submitEdits(view);
            }
        });
        */


        /**
         * Set up the seek bar position display text
         */
        seekPositionDisplay = (TextView) findViewById(R.id.seekDisplayEditCard);
        seekPositionDisplay.setText(R.string.seekDisplayDefault);


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
            Resources resources = getResources();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) { //if seekBar movement is user-initiated, adjust video accordingly
                    videoView.seekTo(progress);
                    currentProgress = progress; //NEED TO CHECK >> MAY NEED TO CONVERT progress TO milliseconds (*1000 to convert??)
                }
                else {
                    seekBar.setProgress(progress);
                }
                seekPositionDisplay.setText(String.format(resources.getString(R.string.seekDisplayEditCard), ((float)progress/1000f)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TO DO
                if(endTimeSwitch) {
                    //This Toast is just for testing purposes
                    //Toast.makeText(getApplicationContext(), "Starting to seek - this will update STOP TIME crop", Toast.LENGTH_SHORT).show();
                }
                else {
                    //This Toast is just for testing purposes
                    //Toast.makeText(getApplicationContext(), "Starting to seek - updating START TIME crop", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TO DO
                //String startString = "Start Time: ";
                //String stopString = "Stop Time: ";
                Resources resources = getResources();
                //boolean wrongOrder;

                //if the switch is set to "Stop"
                if(endTimeSwitch) {
                    //checks is first attempt to adjust OR ensures that endtime comes AFTER start time
                    if(firstAdjustment || timeSelectionCheck(importOptions.startTime, currentProgress)) {
                        Toast.makeText(getApplicationContext(), "Setting STOP TIME crop to: " + currentProgress, Toast.LENGTH_SHORT).show();
                        endTimeText.setText(String.format(resources.getString(R.string.stop_time_default), ((float)currentProgress/1000f)));
                        importOptions.endTime = currentProgress;
                        //importOptions.endTime = videoView.getDuration();
                        firstAdjustment = false;
                        //TEST
                        displayOptions();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "stop time must be AFTER start time (total time at least 2 seconds)", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(firstAdjustment || timeSelectionCheck(currentProgress, importOptions.endTime)) {
                        Toast.makeText(getApplicationContext(), "Setting START TIME crop to: " + currentProgress, Toast.LENGTH_SHORT).show();
                        startTimeText.setText(String.format(resources.getString(R.string.start_time_default), ((float)currentProgress/1000f)));
                        importOptions.startTime = currentProgress;
                        //importOptions.startTime = 0;
                        firstAdjustment = false;
                        //TEST
                        displayOptions();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "start time must be BEFORE end time (total time at least 2 seconds)", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });







        //GET Intent passed from CreateACard Activity
        Intent intent = getIntent(); //get Intent passed from 'CreateCardActivity'
        videoUri = intent.getData(); //get video URI data passed via an Intent from 'CreateCardActivity'

        //initialize import-options structure
        importOptions = new VideoManager.ImportOptions();
        //These members will eventually be set by EditVideo layout
        importOptions.cropRegion = null;
        //importOptions.deleteAfter = false;
        importOptions.startTime = 0;
        //importOptions.endTime = Integer.MAX_VALUE; //will get dynamically set to video duration
        importOptions.quality = 10; //default quality

        if(videoUri == null) {
            importOptions.endTime = 0;
            Toast.makeText(this, "From EditCard: No URI Passed", Toast.LENGTH_SHORT).show(); //pop-up indicating No video passed from 'CreateCardActivity'
        } else {
            //set up video view to initialize 'seekBar' when the video is loaded
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                Resources resources = getResources();
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.postDelayed(onEverySecond, 0); //0 milli-second delay (was 1000)
                    //mediaPlayer.setLooping(true); video will loop indefinitely
                    //set DEFAULT max video length to original video length
                    importOptions.endTime = videoView.getDuration();

                    /**
                     * Set up play/pause button to allow the user to replay and pause the video
                     */
                    playPauseButton = (Button) findViewById(R.id.PlayPauseButtonEdit);
                    playPauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                //seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                //seekPositionDisplay.setText(String.format(resources.getString(R.string.seekDisplayEditCard), ((float)mediaPlayer.getCurrentPosition()/1000f)));
                            }
                            else {
                                //videoView.resume();
                                mediaPlayer.start();
                                //seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                //seekPositionDisplay.setText(String.format(resources.getString(R.string.seekDisplayEditCard), ((float)mediaPlayer.getCurrentPosition()/1000f)));
                            }
                        }
                    });
                }
            });

            videoView.setVideoURI(videoUri); //set view to locate current video needing to be edited

            //calls setOnPreparedListener??
            videoView.start(); //start AFTER setting up the seek bar
        } //end of 'else' //videoUri != null case



        /**Set up start and end time textViews
         *  - initialize to some default value
         *  - adjust when seekBar changed
         */
        startTimeText = (TextView) findViewById(R.id.textViewStartTimeLabel);
        startTimeText.setText(R.string.start_time_label);
        endTimeText = (TextView) findViewById(R.id.textViewStopTimeLabel);
        endTimeText.setText(R.string.stop_time_label);

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
            submitEdits(view);
            //FOR NOW, just return the un-edited video back to 'CreateCard'
            //Will change once connected to backend, and time-cropping is functional

            /*
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
    }


    /**
     *
     * @return false, if the time crop region selected is less than 2000 ms (2 Seconds)
     */
    private boolean timeSelectionCheck(int startTime, int endTime) {
        return ((endTime - startTime) >= 2000);
    }


    /**
     * displays the current import options settings
     *
     * used for testing
     */
    private void displayOptions() {
        Toast.makeText(getApplicationContext(), "Import Options -\nStart: " + importOptions.startTime + "(ms)\nEnd: " + importOptions.endTime +
                "(ms)\nQuality: " + importOptions.quality + "\nCrop Region: " + importOptions.cropRegion, Toast.LENGTH_SHORT).show();
    }



    /**
     * Called when user hits the submit button
     *
     * *NOTE* currently not called, will un-comment in other methods, once back_end connectivity is verified
     *
     * @param view
     */
    private void submitEdits(View view) {
        //MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //retriever.setDataSource(this, videoUri);
        //String endTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Intent returnIntent = new Intent();
        returnIntent.setData(videoUri);
        returnIntent.putExtra(START, importOptions.startTime);
        returnIntent.putExtra(END, importOptions.endTime);
        returnIntent.putExtra(CROP, importOptions.cropRegion); //will be null for MVP
        returnIntent.putExtra(QUALITY, importOptions.quality);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        /*
        //OUTLINE TO CONNECT TO BACKEND
        VideoManager videoEditor = ExternalVideoManager.getInstance(this);
        //TEST
        //NEED TO: determine how to get video file path to pass to backend
        videoEditor.importVideo(this, videoUri, importOptions, new VideoManager.VideoImportListener() {
            @Override
            public void onProgressUpdate(int current, int max) {
                //TO DO: indicate video loading progress bar
                //finish();
            }

            @Override
            public void onComplete(Video vid) {
                //TO DO: once edited, send this video back to 'CreateCardActivity'
                Intent returnIntent = new Intent();
                returnIntent.setData(videoUri);
                returnIntent.putExtra(EDITED_VIDEO, vid);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onFailed(Throwable err) {
                //TO DO: indicate "Failure" to calling routine
                Intent returnIntent = new Intent();
                returnIntent.setData(videoUri);
                //'err' is null -checked
                returnIntent.putExtra(ERROR, err);
                setResult(Activity.RESULT_CANCELED, returnIntent);
                Toast.makeText(getApplicationContext(), "Failed to Import Video", Toast.LENGTH_SHORT).show(); //pop-up indicating No video passed from backend
                finish();
            }
        });
        */
    }





}
