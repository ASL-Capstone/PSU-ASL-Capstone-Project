package com.psu.capstonew17.backend.api;
import android.content.Context;
import android.media.MediaPlayer;
//TODO Get backend.db
//import com.psu.capstonew17.backend.db.AslDbHelper;
import android.content.Context;
import android.media.MediaPlayer;
//import com.example.backendtesting.backend.db.AslDbHelper;

public interface Video {
    /**
     * Configure the supplied MediaPlayer to play this video. Note that this operation may cause
     * errors inside the media player, so you should set its OnErrorListener before calling this
     * method.
     */
    void configurePlayer(MediaPlayer player);
}
