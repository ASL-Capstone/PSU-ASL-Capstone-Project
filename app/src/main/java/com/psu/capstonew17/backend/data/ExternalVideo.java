//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;

import com.psu.capstonew17.backend.EncodeableObject;
import com.psu.capstonew17.backend.api.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class ExternalVideo implements Video, EncodeableObject {
    private File videoFile;
    private int videoId;

    public ExternalVideo(int videoId, File videoFile){
        this.videoId = videoId;
        this.videoFile = videoFile;
    }

    public int getVideoId(){
        return this.videoId;
    }

    @Override
    public void configurePlayer(MediaPlayer player) {

    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            File f = new File(parcel.readString());
            int vid = parcel.readInt();
            return new ExternalVideo(vid, f);
        }

        @Override
        public Object[] newArray(int i) {
            return new ExternalVideo[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoFile.getAbsolutePath());
        parcel.writeInt(videoId);
    }

    private byte[] readFile() throws IOException{
        RandomAccessFile f = new RandomAccessFile(this.videoFile, "r");
        try{
            int length = ((int) f.length());
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }finally {
            f.close();
        }
    }

    @Override
    public byte[] encodeToByteArray() throws IOException{
        byte[] fileBytes = readFile();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(fileBytes);
        return out.toByteArray();
    }
}
