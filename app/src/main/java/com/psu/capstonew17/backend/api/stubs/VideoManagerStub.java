package com.psu.capstonew17.backend.api.stubs;

import android.content.Context;
import android.net.Uri;

import com.psu.capstonew17.backend.api.VideoManager;

import java.io.File;

/**
 * Created by Tim on 4/27/2017.
 */

public class VideoManagerStub implements VideoManager {
    @Override
    public void importVideo(Context ctx, Uri video, ImportOptions options, VideoImportListener handler) {

    }
}
