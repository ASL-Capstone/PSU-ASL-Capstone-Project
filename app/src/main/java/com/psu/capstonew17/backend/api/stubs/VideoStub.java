package com.psu.capstonew17.backend.api.stubs;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.widget.VideoView;

import com.psu.capstonew17.backend.api.Video;

/**
 * Created by Tim on 4/21/2017.
 */

public class VideoStub implements Video {
    @Override
    public int getVideoId(){
        return 0;
    }

    public void configurePlayer(VideoView player){
        return;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }

    public static final Creator<VideoStub> CREATOR = new Parcelable.Creator<VideoStub>() {
        @Override
        public VideoStub createFromParcel(Parcel in) {
            return new VideoStub();
        }

        @Override
        public VideoStub[] newArray(int size) {
            return new VideoStub[size];
        }
    };
}
