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
}
