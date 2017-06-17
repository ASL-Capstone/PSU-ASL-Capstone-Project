//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.CardManager;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.VideoManager;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalDeckManager;
import com.psu.capstonew17.backend.data.ExternalVideoManager;

import java.util.ArrayList;
import java.util.List;

public class CreateCardActivity extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    //cases for the activity result struct
    private static final int GET_VIDEO          = 1;
    private static final int REQUEST_EDIT_VIDEO = 2;

    //cases for the perm request result struct
    private static final int REQ_EXT_STORAGE_PERMS  = 1;
    private static final int REQ_CAMERA_PERMS       = 2;

    //min and max length of an answer for a card
    private static final int MIN_LABEL_LENGTH   = 3;
    private static final int MAX_LABEL_LENGTH   = 250;

    private static final String SELECT_VIDEO = "Select Video";

    //activity elements
    private Button          bttSubmit;
    private Button          bttGetVideo;
    private Button          bttRecordVideo;
    private ListView        listView;
    private Uri             videoUri;
    private EditText        editText;
    private VideoView       videoView;
    private ProgressDialog  progressDialog;

    //a list for the listview and a list for all of the decks
    private List<ListRow>   listRows;
    private List<Deck>      deckList;

    //card attributes
    private String  videoLabel;
    private Video   video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        //submit button should be invisible until a video has been imported.
        bttSubmit = (Button) findViewById(R.id.button_submit);
        bttSubmit.setText(R.string.button_submit);
        bttSubmit.setVisibility(View.GONE);
        bttSubmit.setOnClickListener(this);

        bttGetVideo = (Button) findViewById(R.id.buttonFromGallery);
        bttGetVideo.setOnClickListener(this);

        bttRecordVideo = (Button) findViewById(R.id.buttonRecordVideo);
        bttRecordVideo.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.importing_video));
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);

        editText = (EditText) findViewById(R.id.edit_text_video_answer);
        editText.setOnClickListener(this);

        videoView = (VideoView) findViewById (R.id.videoView_create_card);
        videoView.setVisibility(View.GONE);

        //get the list of all decks in the db
        deckList = ExternalDeckManager.getInstance(this).getDecks(null);

        listRows = new ArrayList<>();
        //populate the list rows for the list view.
        for (int i = 0; i < deckList.size(); i++)
            listRows.add(new ListRow(deckList.get(i).getName(), false));

        //create the list view.
        listView = (ListView) findViewById(R.id.list_items);
        listView.setAdapter(new CustomArrayListAdapter(this, R.layout.list_row, listRows));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            //user wants to record a video for the new card
            case R.id.buttonRecordVideo:
                //check perms, if we don't have then then request them
                if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                        PackageManager.PERMISSION_GRANTED ==
                                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                    dispatchTakeVideoIntent();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA_PERMS);
                }
                break;

            //user wants to use a video from the gallery. check for external storage perms.
            //if we don't have them, then request them.
            case R.id.buttonFromGallery:
                dispatchGalleryIntent();
                break;

            //user is finished creating this card.
            case R.id.button_submit:
                videoLabel = editText.getText().toString().trim();
                //make sure that the video imported successfully
                //and that the length of the answer is in valid range
                if (!videoLabelCheck() || video == null){
                    Toast.makeText(this,
                            R.string.import_video_error, Toast.LENGTH_SHORT).show();

                } else {
                    CardManager cardManager = ExternalCardManager.getInstance(this);
                    try {
                        Log.d("Hang Debug", "trying to make card");
                        //video and label look good, create the card.
                        Card card = cardManager.buildCard(video, videoLabel);
                        Log.d("Hang Debug", "card made");
                        //add the card to all of the selected decks.
                        for(int i = 0; i < listRows.size(); i++){
                            ListRow curr = listRows.get(i);
                            if (curr.isChecked){
                                Deck slctdDeck = deckList.get(i);
                                List<Card> cards = slctdDeck.getCards();
                                cards.add(card);
                                slctdDeck.commit();
                            }
                        }
                        Log.d("Hang Debug", "about to call finish");
                        finish();
                        //cards can't have the same answer and video!
                    } catch (ObjectAlreadyExistsException e){
                        Toast.makeText(this, R.string.card_already_exists, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //gets the result of any perms we requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, perms, grantResults);
        switch(requestCode) {
            //if we needed camera perms
            case REQ_CAMERA_PERMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakeVideoIntent();
                } else {
                    Toast.makeText(this, R.string.card_camera_perm_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //if the label the correct length?
    private boolean videoLabelCheck() {
        //trim it to remove leading or training white space when you get it
        videoLabel = editText.getText().toString().trim();
        if (videoLabel.length() < MIN_LABEL_LENGTH || videoLabel.length() > MAX_LABEL_LENGTH) {
            //the user's answer is too short or long, let them now so they can fix it.
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getResources().getString(R.string.card_name_lngth_error1));
            stringBuilder.append(MIN_LABEL_LENGTH);
            stringBuilder.append(getResources().getString(R.string.card_name_lngth_error2));
            stringBuilder.append(MAX_LABEL_LENGTH);

            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //call/display the gallery so that the user can select a video.
    private void dispatchGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, SELECT_VIDEO),
                GET_VIDEO);
    }

    //call/display the camera so that the user can record a video.
    //"Alright Mr. DeMille, I'm ready for my closeup!"
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent();
        takeVideoIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, GET_VIDEO);
        } else{
            Toast.makeText(getApplicationContext(),
                    R.string.card_record_video_error, Toast.LENGTH_SHORT).show();
        }
    }

    //poorly named, this actually starts the video and makes hidden views visible
    protected void startVideo(){
        video.configurePlayer(videoView);
        videoView.setVisibility(View.VISIBLE);
        bttSubmit.setVisibility(View.VISIBLE);
        videoView.start();
    }

    //poorly named, this actually starts the video and hides views
    protected void stopVideo(){
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
        bttSubmit.setVisibility(View.GONE);
    }

    //something went wrong while importing the video.
    //couldn't Toast in onFailed, and I'm lazy.
    protected void videoErrorToast(){
        Toast.makeText(this,
                R.string.import_video_error, Toast.LENGTH_SHORT).show();
    }

    //get the results from activities that...
    // ...return results...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            //We be getting a video! yay!
            case GET_VIDEO:
                if (resultCode == RESULT_OK) {
                    videoUri = intent.getData();


                    //let the user edit it!
                    if(videoUri != null){
                        //Verify length of video is greater than two seconds
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(this, videoUri);
                        String endTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        if(Integer.parseInt(endTime) < 2000){
                            Toast.makeText(this, "Video length must be at least 2 seconds", Toast.LENGTH_SHORT);
                        }
                        else{
                            intent = new Intent(this, EditVideoActivity.class);
                            intent.setData( videoUri);
                            startActivityForResult(intent, REQUEST_EDIT_VIDEO);
                        }
                    }
                }
                break;

            //They're done editing it! What are the import options now?
            case REQUEST_EDIT_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    //if they had previously imported a video but changed their minds and imported
                    //a different video then we need to stop payback of the old one and hide some
                    //views while the new one imports.
                    stopVideo();

                    //lets get the import options the user wants
                    VideoManager.ImportOptions imo = new VideoManager.ImportOptions();
                    Bundle bundle = intent.getExtras();
                    imo.startTime   = bundle.getInt(EditVideoActivity.START);
                    imo.endTime     = bundle.getInt(EditVideoActivity.END);
                    imo.quality     = bundle.getInt(EditVideoActivity.QUALITY);
                    imo.cropRegion  = bundle.getParcelable(EditVideoActivity.CROP);

                    VideoManager vm = ExternalVideoManager.getInstance(this);
                    //block with a spin wheel while the video is imported
                    progressDialog.show();
                    //now we can import the new video.
                    //this is tempermental on samsung devices
                    vm.importVideo(this, videoUri, imo, new VideoManager.VideoImportListener() {
                        //this is behaving weirdly with a horizontal progress bar, so for now I'm
                        //ignoring it in favor of a spin wheel
                        @Override
                        public void onProgressUpdate(int current, int max) {
                        }

                        //this is called when the import has completed
                        //we get the video, display it and the submit button,
                        //and then hide the progress dialog.
                        @Override
                        public void onComplete(Video vid) {
                            video = vid;
                            startVideo();
                            progressDialog.dismiss();
                        }

                        //if importing fails.
                        //I can't toast here
                        @Override
                        public void onFailed(Throwable err) {
                            videoErrorToast();
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
        }
    }
}

