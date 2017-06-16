//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.psu.capstonew17.backend.api.*;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.video.PreprocessingPipeline;
import com.psu.capstonew17.backend.video.StubPreprocessingPipeline;


public class ExternalVideoManager implements VideoManager {
    static ExternalVideoManager INSTANCE = new ExternalVideoManager();

    private AslDbHelper dbHelper;

    public static VideoManager getInstance(Context context){
        INSTANCE.dbHelper = AslDbHelper.getInstance(context);
        return INSTANCE;
    }

    AslDbHelper getDbHelper(){
        return dbHelper;
    }

    Video getVideo(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = dbHelper.buildSelectQuery(
                VideoEntry.TABLE_NAME,
                Arrays.asList(VideoEntry.COLUMN_ID + "=" + Integer.toString(id))
        );
        Video video = null;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            String videoPath = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_PATH));
            File videoFile = new File(videoPath);
            video = new ExternalVideo(id, videoFile.getAbsoluteFile());
        }
        cursor.close();
        return video;
    }

    public Video decodeVideo(byte [] bytes){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA256");
        } catch(NoSuchAlgorithmException e) {
            Log.d("VideoManager", "System does not support SHA256 hash", e);
            return null;
        }

        // generate an output file location
        final File outFile = new File(Environment.getDataDirectory().getAbsoluteFile(),
                UUID.randomUUID().toString());

        Video video = null;
        byte[] sha = null;
        try {
            InputStream input = new ByteArrayInputStream(bytes);
            OutputStream output = new FileOutputStream(outFile);
            byte [] data = new byte[8192];
            int len;
            while((len = input.read(data)) != -1){
                output.write(data, 0, len);
                digest.update(data, 0, len);
            }
            sha = digest.digest();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String query = dbHelper.buildSelectQuery(
                    VideoEntry.TABLE_NAME,
                    Arrays.asList(VideoEntry.COLUMN_SHA + "='" + Base64.encodeToString(sha, Base64.DEFAULT) + "'")
            );
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                int videoId = cursor.getInt(cursor.getColumnIndex(VideoEntry.COLUMN_ID));
                String videoPath = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_PATH));
                File existingVideo = new File(videoPath);
                outFile.delete();
                video = new ExternalVideo(videoId, existingVideo.getAbsoluteFile());
            }
            else{
                // create the new video
                ContentValues values = new ContentValues();
                values.put(VideoEntry.COLUMN_PATH, outFile.getAbsolutePath());
                values.put(VideoEntry.COLUMN_SHA, Base64.encodeToString(sha, Base64.DEFAULT));
                int videoId = (int) db.insert(VideoEntry.TABLE_NAME, null, values);
                video = new ExternalVideo(videoId, outFile.getAbsoluteFile());
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return video;
    }

    @Override
    public void importVideo(Context ctx, final Uri video, final ImportOptions options, VideoImportListener handler) {
        final VideoImportListener handle = handler;

        // generate an output file location
        final File outFile = new File(ctx.getFilesDir(), UUID.randomUUID().toString());

        PreprocessingPipeline  pipeline;
        try {
            pipeline = new StubPreprocessingPipeline(ctx, outFile, video, options);
        } catch(IOException e) {
            handler.onFailed(e);
            return;
        }

        pipeline.setListener(new PreprocessingPipeline.PreprocessingListener() {
            @Override
            public void onCompleted() {
                MessageDigest digest;
                try {
                    digest = MessageDigest.getInstance("SHA256");
                } catch(NoSuchAlgorithmException e) {
                    Log.d("VideoManager", "System does not support SHA256 hash", e);
                    outFile.delete();
                    handle.onFailed(e);
                    return;
                }

                byte[] sha = null;
                try {
                    InputStream istrm = new FileInputStream(outFile);
                    byte[] arr = new byte[8192];
                    int len;
                    while((len = istrm.read(arr)) > 0) {
                        digest.update(arr, 0, len);
                    }
                    sha = digest.digest();
                } catch(IOException e) {
                    Log.d("VideoManager", "Unexpected IO error", e);
                    outFile.delete();
                    handle.onFailed(e);
                    return;
                }
                // check if video already exists, if so delete the video file we just created
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String query = dbHelper.buildSelectQuery(
                        VideoEntry.TABLE_NAME,
                        Arrays.asList(VideoEntry.COLUMN_SHA + "='" + Base64.encodeToString(sha, Base64.DEFAULT) + "'")
                );
                Video video;
                Cursor cursor = db.rawQuery(query, null);
                if(cursor.moveToFirst()){
                    int videoId = cursor.getInt(cursor.getColumnIndex(VideoEntry.COLUMN_ID));
                    String videoPath = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_PATH));
                    File existingVideo = new File(videoPath);
                    outFile.delete();
                    video = new ExternalVideo(videoId, existingVideo.getAbsoluteFile());
                }
                else{
                    // create the new video
                    ContentValues values = new ContentValues();
                    values.put(VideoEntry.COLUMN_PATH, outFile.getAbsolutePath());
                    values.put(VideoEntry.COLUMN_SHA, Base64.encodeToString(sha, Base64.DEFAULT));
                    int videoId = (int) db.insert(VideoEntry.TABLE_NAME, null, values);
                    video = new ExternalVideo(videoId, outFile.getAbsoluteFile());
                }
                cursor.close();
                handle.onComplete(video);
            }

            @Override
            public void onProgress(int c, int m) { handle.onProgressUpdate(c, m); }

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
