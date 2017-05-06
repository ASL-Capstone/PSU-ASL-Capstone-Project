package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.psu.capstonew17.backend.api.*;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.video.PreprocessingPipeline;


public class ExternalVideoManager implements VideoManager {
    public static ExternalVideoManager INSTANCE = new ExternalVideoManager();

    private AslDbHelper dbHelper;

    public static VideoManager getInstance(Context context){
        INSTANCE.dbHelper = new AslDbHelper(context);
        return INSTANCE;
    }

    public AslDbHelper getDbHelper(){
        return dbHelper;
    }

    public Video getVideo(int id){
        return null;
    }


    @Override
    public void importVideo(final File videoFile, ImportOptions options, VideoImportListener handler) {
        final VideoImportListener handle = handler;

        // generate an output file location
        final File outFile = new File(Environment.getDataDirectory().getAbsoluteFile(),
                UUID.randomUUID().toString());

        PreprocessingPipeline  pipeline;
        try {
            pipeline = new PreprocessingPipeline(outFile, videoFile);
        } catch(IOException e) {
            handler.onFailed(e);
            return;
        }

        pipeline.setListener(new PreprocessingPipeline.PreprocessingListener() {
            @Override
            public void onCompleted() {
                // create the new video
                dbHelper = ExternalVideoManager.INSTANCE.getDbHelper();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(VideoEntry.COLUMN_PATH, outFile.getAbsolutePath());
                int videoId = (int) db.insert(VideoEntry.TABLE_NAME, null, values);
                Video video = new ExternalVideo(videoId, outFile.getAbsoluteFile());
                handle.onComplete(video);
            }

            @Override
            public void onFailed() {
                handle.onFailed(null);
            }
        });
        try {
            pipeline.start();
        } catch(IOException e) {
            handler.onFailed(e);
            return;
        }
    }
}
