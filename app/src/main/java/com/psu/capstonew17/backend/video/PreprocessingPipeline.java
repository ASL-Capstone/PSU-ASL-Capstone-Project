//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.video;

import android.content.Context;
import android.graphics.Rect;
import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.psu.capstonew17.backend.api.VideoManager;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A pipeline of video processing steps, associated with a frame source and output file.
 */
public class PreprocessingPipeline {
    protected File output;
    protected Uri input;
    protected VideoManager.ImportOptions options;
    protected Context ctx;

    protected PreprocessingListener listener = null;

    public interface PreprocessingListener {
        void onCompleted();
        void onFailed();
        void onProgress(int current, int max);
    }

    public PreprocessingPipeline(Context ctx, File outFile, Uri in, VideoManager.ImportOptions opts) throws IOException {
        this.ctx = ctx;
        output = outFile;
        input = in;
        options = opts;
    }

    public void setListener(PreprocessingListener l) {
        listener = l;
    }

    public void start() throws IOException {
        PreprocessingOperation op = new PreprocessingOperation();
        op.execute(Pair.create(input, output));
    }

    private class PreprocessingOperation extends AsyncTask<Pair<Uri, File>, Pair<Integer, Integer>, Void> {
        @Override
        protected Void doInBackground(Pair<Uri, File>... inputs) {
            Uri in = inputs[0].first;
            File out = inputs[0].second;

            final MediaRecorder record;
            final MediaExtractor extractor;
            final MediaCodec decoder;
            MediaFormat inputFormat;

            // open the input to figure out the target size
            int srcWidth, srcHeight;
            int videoTrack = -1;
            extractor = new MediaExtractor();
            MediaFormat trackFmt = null;
            try {
                extractor.setDataSource(ctx, in, null);
                for (int i = 0; i < extractor.getTrackCount(); i++) {
                    trackFmt = extractor.getTrackFormat(i);
                    if (trackFmt.getString(MediaFormat.KEY_MIME).startsWith("video")) {
                        videoTrack = i;
                        extractor.selectTrack(i);
                        break;
                    }
                }
                if (videoTrack == -1) throw new RuntimeException("No video tracks available");
                decoder = MediaCodec.createDecoderByType(trackFmt.getString(MediaFormat.KEY_MIME));
                inputFormat = trackFmt;
            } catch(IOException e) {
                cancel(false);
                return null;
            }
            srcWidth = trackFmt.getInteger(MediaFormat.KEY_WIDTH);
            srcHeight = trackFmt.getInteger(MediaFormat.KEY_HEIGHT);

            // try to figure out what orientation the source video uses
            int rotation = 0;
            if(trackFmt.containsKey(MediaFormat.KEY_ROTATION))
                rotation = trackFmt.getInteger(MediaFormat.KEY_ROTATION);

            // figure out target width/height and format
            MediaFormat outFormat = trackFmt;

            // set up a muxer
            MediaMuxer muxer;
            int videoTrackIdx;
            try {
                muxer = new MediaMuxer(out.getPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                videoTrackIdx = muxer.addTrack(outFormat);
            } catch(IOException e) {
                Log.e("VPreproc", "Muxer I/O error", e);
                cancel(false);
                return null;
            }
            muxer.setOrientationHint(rotation); // configure output orientation
            Void res = doStreamCopy(extractor, videoTrackIdx, muxer);
            extractor.release();
            return res;
        }

        private Void doStreamCopy(MediaExtractor extract, int strmIdx, MediaMuxer muxer) {
            MediaCodec.BufferInfo bufInfo = new MediaCodec.BufferInfo();
            ByteBuffer buf = ByteBuffer.allocate(8192*1024); // transfer in 8K units
            muxer.start();
            while(true) {
                // try to provide input to the muxer
                int length = extract.readSampleData(buf, 0);
                if(length == -1) break;
                bufInfo.presentationTimeUs = extract.getSampleTime();
                bufInfo.offset = 0;
                bufInfo.size = length;
                bufInfo.flags = extract.getSampleFlags();
                /*
                if((bufInfo.presentationTimeUs/1000 >= options.startTime) &&
                        (bufInfo.presentationTimeUs/1000 <= options.endTime))
                    */
                    muxer.writeSampleData(strmIdx, buf, bufInfo);
                extract.advance();
            }
            muxer.stop();
            muxer.release();
            return null;
        }

        @Override
        protected void onProgressUpdate(Pair<Integer, Integer>... values) {
            if(listener != null) listener.onProgress(values[0].first, values[0].second);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(listener != null) {
                listener.onCompleted();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(listener != null) {
                listener.onFailed();
            }
        }
    }
}
