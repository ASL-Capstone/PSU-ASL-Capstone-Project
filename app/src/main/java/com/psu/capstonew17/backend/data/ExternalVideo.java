//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.media.MediaPlayer;
import com.psu.capstonew17.backend.api.*;

import java.io.File;

class ExternalVideo implements Video {
    private File videoFile;
    private int videoId;

    public ExternalVideo(int videoId, File videoFile){
        this.videoId = videoId;
        this.videoFile = videoFile;
    }

    public int getVideoId(){
        return this.videoId;
    }

    @Override
    public void configurePlayer(MediaPlayer player) {

    }
}
