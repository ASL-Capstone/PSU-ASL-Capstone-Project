package com.psu.capstonew17.pdxaslapp;

import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.VideoManager;
import com.psu.capstonew17.backend.api.stubs.VideoStub;
import com.psu.capstonew17.backend.api.stubs.VideoManagerStub;
import com.psu.capstonew17.backend.data.ExternalVideoManager;
import com.psu.capstonew17.backend.api.VideoManager.VideoImportListener;
import com.psu.capstonew17.backend.db.AslDbHelper;
import android.app.ProgressDialog;



import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.widget.Toast;



import java.io.File;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tim on 5/8/2017.
 */

public class VideoInstrumentalTest {
    private Context context = InstrumentationRegistry.getTargetContext();
    private VideoView       videoView;
    private Video   video;
    private AslDbHelper dbHelper;
    private Button bttSubmit;
    private ProgressDialog  progressDialog;

    Video vid1;
    Video vid2;


    @Test
    public void configurePlayer() {
        VideoManager manager = ExternalVideoManager.getInstance(context);
        Video video = new VideoStub();
        MediaPlayer player = new MediaPlayer();


        video.configurePlayer(videoView);
    }

    protected void startVideo(){
        video.configurePlayer(videoView);
        videoView.setVisibility(View.VISIBLE);
        bttSubmit.setVisibility(View.VISIBLE);
        videoView.start();
    }
    protected void videoErrorToast(){
        Toast.makeText(context,
                R.string.import_video_error, Toast.LENGTH_SHORT).show();
    }

    @Test
    public void importVideo() {
        VideoManager manager = ExternalVideoManager.getInstance(context);
        dbHelper = AslDbHelper.getInstance(context);
        dbHelper.clearTables(dbHelper.getWritableDatabase());

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri videoUri = intent.getData();
        VideoManager.ImportOptions options = new VideoManager.ImportOptions();

        Rect rect = new Rect(2, 2, 2, 2);
        options.startTime = 0;
        options.endTime = 5;
        options.quality = 5;
        options.cropRegion = rect;

        /*
        manager.importVideo(context, videoUri, options, new VideoManager.VideoImportListener() {
            @Override
            public void onProgressUpdate(int current, int max) {
            }

            @Override
            public void onComplete(Video vid) {
                vid1 = vid;
                startVideo();
                progressDialog.dismiss();
            }

            @Override
            public void onFailed(Throwable err) {
                videoErrorToast();
                progressDialog.dismiss();
                throw new AssertionError();
            }
        });
        /*
        manager.importVideo(context, videoUri, options, new VideoManager.VideoImportListener() {
            @Override
            public void onProgressUpdate(int current, int max) {
            }

            @Override
            public void onComplete(Video vid) {
                vid2 = vid;
                assertEquals(vid1, vid2);
            }

            @Override
            public void onFailed(Throwable err) {
                throw new AssertionError();
            }
        });*/

    }

}
