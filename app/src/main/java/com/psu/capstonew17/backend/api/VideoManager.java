//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.api;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;

import java.io.File;
import java.lang.Throwable;

public interface VideoManager {

    public class ImportOptions {
        /**
         * The crop region for this video, or null to disable cropping.
         */
        public Rect cropRegion;

        /**
         * The start time (in milliseconds) of the first processed frame.
         */
        public int startTime;

        /**
         * The end time (in milliseconds) of the first processed frame.
         */
        public int endTime;

        /**
         * Define the maximum acceptable quality degradation as a result of
         * encoding. Ranges from 1 to 20, 20 being the best quality.
         */
        public int quality;
    }

    /**
     * Listener interface for monitoring vidoe import processes
     */
    public interface VideoImportListener {
        /**
         * Called on a progress update
         * 
         * @param current The current progress value
         * @param max The maximum progress value
         */
        void onProgressUpdate(int current, int max);

        /**
         * Called when the video import is complete
         * 
         * @param vid The resulting video
         */
        void onComplete(Video vid);

        /**
         * Called when the video import fails due to an error
         */
        void onFailed(Throwable err);
    }

    /**
     * Import a video file with the given options.
     *
     * When complete, call appropriate methods in the passed video import
     * listener.
     *
     * @param video file to import. Must be readable.
     * @param options The options to use when importing the video
     * @param handler The listener which will be notified of completion
     */
    void importVideo(Context ctx, Uri video, ImportOptions options, VideoImportListener handler);
}
