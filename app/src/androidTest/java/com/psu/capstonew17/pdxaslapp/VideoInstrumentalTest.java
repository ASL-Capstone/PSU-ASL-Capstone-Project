package com.psu.capstonew17.pdxaslapp;

import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.VideoManager;
import com.psu.capstonew17.backend.data.ExternalVideoManager;
import com.psu.capstonew17.backend.api.VideoManager.VideoImportListener;
import com.psu.capstonew17.backend.db.AslDbContract;
import com.psu.capstonew17.backend.db.AslDbHelper;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class VideoInstrumentalTest {
    private Context context;
    private AslDbHelper dbHelper;
    private ExternalVideoManager videoManager;
    VideoManager.VideoImportListener listener;
    File tempFile;
    long currentTime;
    Video video;

    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = AslDbHelper.getInstance(context);
        videoManager = (ExternalVideoManager) ExternalVideoManager.getInstance(context);
        listener = mock(VideoImportListener.class);
        if(tempFile != null && tempFile.exists()){
            tempFile.delete();
        }
        currentTime = System.currentTimeMillis();
    }

    private void cleanUp(Video video){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                AslDbContract.VideoEntry.TABLE_NAME,
                AslDbContract.VideoEntry.COLUMN_ID + "=" + video.getVideoId(),
                null
        );
        if(tempFile != null && tempFile.exists()){
            tempFile.delete();
        }
        // delete the file that was just created by the import
        for(File f : context.getFilesDir().listFiles()){
            if(f.lastModified() - currentTime <= 10000){
                f.delete();
            }
        }
    }

    @Test
    public void importVideo() throws Exception {
        VideoManager.ImportOptions options = new VideoManager.ImportOptions();
        Rect rect = new Rect(2, 2, 2, 2);
        options.startTime = 0;
        options.endTime = 5;
        options.quality = 5;
        options.cropRegion = rect;

        tempFile = File.createTempFile("test.mp4", null, context.getCacheDir());

        videoManager.importVideo(context, Uri.fromFile(tempFile), options, new VideoManager.VideoImportListener() {
            @Override
            public void onProgressUpdate(int current, int max) {
                assertTrue(current <= max);
            }

            @Override
            public void onComplete(Video vid) {
                video = vid;
                int expected = vid.getVideoId();
                int actual = videoManager.getVideo(expected).getVideoId();
                assertEquals(expected, actual);
                cleanUp(vid);
            }

            @Override
            public void onFailed(Throwable err) {
                throw new AssertionError();
            }
        });
    }

}
