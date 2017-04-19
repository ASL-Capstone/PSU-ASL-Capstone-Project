package com.psu.capstonew17.backend.api;
import android.media.MediaPlayer;

public interface Video {
    /**
     * Configure the supplied MediaPlayer to play this video. Note that this operation may cause
     * errors inside the media player, so you should set its OnErrorListener before calling this
     * method.
     */
    void configurePlayer(MediaPlayer player);
}
