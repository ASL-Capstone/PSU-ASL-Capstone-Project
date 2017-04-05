package com.example.backendtesting.backend.api;


import android.content.Context;
import android.util.Base64;

import com.example.backendtesting.backend.db.AslDbHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VideoManager {
    private AslDbHelper dbHelper;

    public VideoManager(Context context){
        dbHelper = new AslDbHelper(context);
    }

    private String calculateSha(String path){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(path.getBytes());
        String sha = Base64.encodeToString(md.digest(), Base64.DEFAULT);
        return sha;
    }

    public Video saveVideo(String pathToVideo){
        String videoSha = calculateSha(pathToVideo);
        Video video = findVideo(videoSha);
        if(video == null){
            return dbHelper.insertVideo(pathToVideo, calculateSha(pathToVideo));
        }
        return video;
    }

    public Video findVideo(String videoSha){
        return dbHelper.findVideo(videoSha);
    }
}
