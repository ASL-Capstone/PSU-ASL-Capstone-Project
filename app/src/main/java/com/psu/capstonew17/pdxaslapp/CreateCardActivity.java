//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private ListView listView;
    private List<ListRow> listRows = new ArrayList<>();
    List<Deck> deckList;
    private CardManager cardManager;

    private CustomArrayListAdapter myAdapter;
    private Uri videoUri;

    static final int GET_VIDEO = 1;
    static final int REQUEST_EDIT_VIDEO = 3;
    public static final String START = "start";
    public static final String END = "end";
    public static final String QUALITY = "quality";
    private static final String CROP = "cropRegion";

    static final int MIN_LABEL_LENGTH = 3;
    static final int MAX_LABEL_LENGTH = 250;

    static final int REQ_CAMERA_PERMS = 4;

    private final String SELECT_VIDEO = "Select Video";

    private Button bttSubmit;
    private Button bttGetVideo;
    private Button bttRecordVideo;
    private EditText editText;
    private VideoView videoView;
    private ProgressDialog progressDialog;
    private Card card;

    private String videoLabel;
    private Video video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        //submit button should be invisible until a video has been imported.
        bttSubmit = (Button) findViewById(R.id.button_submit);
        bttSubmit.setText(R.string.button_submit);
        bttSubmit.setVisibility(View.INVISIBLE);
        bttSubmit.setOnClickListener(this);

        bttGetVideo = (Button) findViewById(R.id.buttonFromGallery);
        bttGetVideo.setOnClickListener(this);

        bttRecordVideo = (Button) findViewById(R.id.buttonRecordVideo);
        bttRecordVideo.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Importing video");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);

        editText = (EditText) findViewById(R.id.edit_text_video_answer);
        editText.setOnClickListener(this);

        videoView = (VideoView) findViewById (R.id.videoView_create_card);
        videoView.setVisibility(View.INVISIBLE);

        //get the list of all decks in the db
        deckList = ExternalDeckManager.getInstance(this).getDecks(null);

        //populate the list rows for the list view.
        for (int i = 0; i < deckList.size(); i++)
            listRows.add(new ListRow(deckList.get(i).getName(), false));

        //create the list view.
        listView = (ListView) findViewById(R.id.list_items);
        myAdapter =  new CustomArrayListAdapter(this, R.layout.list_row, listRows);
        listView.setAdapter(myAdapter);
        listView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.buttonRecordVideo:
                if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                        PackageManager.PERMISSION_GRANTED ==
                                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                    dispatchTakeVideoIntent();

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA_PERMS);
                }
                break;

            case R.id.buttonFromGallery:
                intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, SELECT_VIDEO),
                        GET_VIDEO);
                break;

                case R.id.button_submit:
                videoLabel = editText.getText().toString();

                if (!videoLabelCheck() || video == null){
                    Toast.makeText(this,
                            R.string.import_video_error, Toast.LENGTH_SHORT).show();

                } else {
                    cardManager = ExternalCardManager.getInstance(this);
                    try {
                        card = cardManager.buildCard(video, videoLabel);
                        for(int i = 0; i < listRows.size(); i++){
                            ListRow curr = listRows.get(i);
                            if (curr.isChecked){
                                Deck slctdDeck = deckList.get(i);
                                List<Card> cards = slctdDeck.getCards();
                                cards.add(card);
                                slctdDeck.commit();
                            }
                        }
                        finish();
                    } catch (ObjectAlreadyExistsException e){
                        //toast here
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String perms[], int[] grantResults){
        switch(requestCode) {
            case REQ_CAMERA_PERMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakeVideoIntent();
                } else {
                    Toast.makeText(this, R.string.card_camera_perm_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean videoLabelCheck() {

        videoLabel = editText.getText().toString();
        if (videoLabel.length() < MIN_LABEL_LENGTH || videoLabel.length() > MAX_LABEL_LENGTH) {
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

    private boolean videoFileCheck() {
        if (videoUri == null) {
            Toast.makeText(this, R.string.card_uri_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent();
        takeVideoIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, GET_VIDEO);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    R.string.card_record_video_error, Toast.LENGTH_SHORT).show();
        }
    }

    protected void startVideo(){
        video.configurePlayer(videoView);
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
    }

    protected void stopVideo(){
        videoView.stopPlayback();
        videoView.setVisibility(View.INVISIBLE);
        bttSubmit.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
    }

    protected void videoErrorToast(){
        Toast.makeText(this,
                R.string.import_video_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case GET_VIDEO:
                if (resultCode == RESULT_OK) {
                    videoUri = intent.getData();

                    if(videoUri != null){
                        intent = new Intent(this, EditVideoActivity.class);
                        intent.setData( videoUri);
                        startActivityForResult(intent, REQUEST_EDIT_VIDEO);
                    }
                }
                break;

            case REQUEST_EDIT_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    stopVideo();
                    VideoManager.ImportOptions imo = new VideoManager.ImportOptions();
                    Bundle bundle = intent.getExtras();
                    imo.startTime   = bundle.getInt(START);
                    imo.endTime     = bundle.getInt(END);
                    imo.quality     = bundle.getInt(QUALITY);
                    imo.cropRegion  = bundle.getParcelable(CROP);

                    VideoManager vm = ExternalVideoManager.getInstance(this);
                    progressDialog.show();
                    vm.importVideo(this, videoUri, imo, new VideoManager.VideoImportListener() {
                        @Override
                        public void onProgressUpdate(int current, int max) {
                        }

                        @Override
                        public void onComplete(Video vid) {
                            video = vid;
                            progressDialog.hide();
                            startVideo();
                            bttSubmit.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onFailed(Throwable err) {
                                videoErrorToast();
                            }
                    });
                }
                break;
        }
    }
}

