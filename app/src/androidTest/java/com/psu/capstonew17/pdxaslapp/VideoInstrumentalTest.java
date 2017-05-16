package com.psu.capstonew17.pdxaslapp;

import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.VideoManager;
import com.psu.capstonew17.backend.api.stubs.VideoStub;
import com.psu.capstonew17.backend.api.stubs.VideoManagerStub;
import com.psu.capstonew17.backend.data.ExternalVideoManager;
import com.psu.capstonew17.backend.api.VideoManager.VideoImportListener;



import android.media.MediaPlayer;


import java.io.File;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tim on 5/8/2017.
 */

public class VideoInstrumentalTest {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void configurePlayer() {
        VideoManager manager = ExternalVideoManager.getInstance(context);
        Video video = new VideoStub();
        MediaPlayer player = new MediaPlayer();

        //Don't know where the onErrorListener is

        video.configurePlayer(player);
    }

    /*
    @Test
    public void importVideo() {
        VideoManager manager = ExternalVideoManager.getInstance(context);

        VideoImportListener listener = ExternalVideoManager.VideoImportListener.
        //manager.importVideo();
    }*/

}
