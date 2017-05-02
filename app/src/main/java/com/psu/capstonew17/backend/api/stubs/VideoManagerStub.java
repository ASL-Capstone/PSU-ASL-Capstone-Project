package com.psu.capstonew17.backend.api.stubs;

import com.psu.capstonew17.backend.api.VideoManager;

import java.io.File;

/**
 * Created by Tim on 4/27/2017.
 */

public class VideoManagerStub implements VideoManager {
    @Override
    public void importVideo(File videoFile, ImportOptions options, VideoImportListener handler) {

    }
}
