package com.example.backendtesting.backend.api;

import android.content.Context;
import com.example.backendtesting.backend.db.AslDbHelper;

public class Video {
    public int videoId;
    public String videoPath;
    public String videoSha;

    public Video(int id, String path, String sha){
        videoId = id;
        videoPath = path;
        videoSha = sha;
    }

    public static Video saveVideo(String path, Context context){
        AslDbHelper dbHelper = new AslDbHelper(context);
        // TODO: generate a sha from video, check if it exists before duplicating
        return dbHelper.insertVideo(path, "somesha123456");
    }
}
