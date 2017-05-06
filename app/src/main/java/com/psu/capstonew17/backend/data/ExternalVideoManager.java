//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.Context;

import com.psu.capstonew17.backend.api.*;


import java.io.File;
import com.psu.capstonew17.backend.db.AslDbHelper;


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
    public void importVideo(File videoFile, ImportOptions options, VideoImportListener handler) {

    }
}
