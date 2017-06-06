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
            try {
                extractor.setDataSource(ctx, in, null);
                MediaFormat trackFmt = null;
                for (int i = 0; i < extractor.getTrackCount(); i++) {
                    trackFmt = extractor.getTrackFormat(i);
                    if (trackFmt.getString(MediaFormat.KEY_MIME).startsWith("video")) {
                        videoTrack = i;
                        extractor.selectTrack(i);
                        break;
                    }
                }
                if (videoTrack == -1) throw new RuntimeException("No video tracks available");
                srcWidth = trackFmt.getInteger(MediaFormat.KEY_WIDTH);
                srcHeight = trackFmt.getInteger(MediaFormat.KEY_HEIGHT);
                decoder = MediaCodec.createDecoderByType(trackFmt.getString(MediaFormat.KEY_MIME));
                inputFormat = trackFmt;
            } catch(IOException e) {
                cancel(false);
                return null;
            }

            // prime the decoder
            long maxTime = inputFormat.getLong(MediaFormat.KEY_DURATION);
            {
                decoder.configure(inputFormat, null, null, 0);
                decoder.start();
                int inIdx;
                while((inIdx = decoder.dequeueInputBuffer(10)) >= 0) {
                    // fill the buffer
                    ByteBuffer ibuf = decoder.getInputBuffer(inIdx);
                    int length = extractor.readSampleData(ibuf, 0);
                    if (length == -1) {
                        decoder.queueInputBuffer(inIdx, 0, 0, -1,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        break;
                    }
                    extractor.advance();
                    decoder.queueInputBuffer(inIdx, 0, length, -1, 0);
                }
                inputFormat = decoder.getOutputFormat();
                srcWidth = inputFormat.getInteger(MediaFormat.KEY_WIDTH);
                srcHeight = inputFormat.getInteger(MediaFormat.KEY_HEIGHT);
            }

            // figure out target width/height and format
            //int tgtWidth = srcWidth/2, tgtHeight = srcHeight/2;
            int tgtWidth = srcWidth, tgtHeight = srcHeight; // TODO: Resizing
            MediaFormat outFormat = MediaFormat.createVideoFormat("video/x-vnd.on2.vp8", tgtWidth, tgtHeight);
            outFormat.setInteger(MediaFormat.KEY_BIT_RATE, 20*1024*1024); // 20 Mbps
            outFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, inputFormat.getInteger(MediaFormat.KEY_COLOR_FORMAT));
            if(inputFormat.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                outFormat.setInteger(MediaFormat.KEY_FRAME_RATE, inputFormat.getInteger(MediaFormat.KEY_FRAME_RATE));
                outFormat.setInteger(MediaFormat.KEY_CAPTURE_RATE, inputFormat.getInteger(MediaFormat.KEY_FRAME_RATE));
            } else {
                outFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 60);
                outFormat.setInteger(MediaFormat.KEY_CAPTURE_RATE, 60);
            }
            outFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 60);

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
            return doStreamCopy(extractor, videoTrackIdx, muxer);

            /*
            MediaCodec encoder;
            try {
                String cname = new MediaCodecList(MediaCodecList.REGULAR_CODECS).findEncoderForFormat(outFormat);
                if(cname == null) {
                    Log.wtf("VPreproc", "Codec not found.");
                    Log.d("VPreproc", "Valid codecs: ");
                    for(MediaCodecInfo i : new MediaCodecList(MediaCodecList.REGULAR_CODECS).getCodecInfos()) {
                        if(!i.isEncoder()) continue;
                        for(String s : i.getSupportedTypes())
                            Log.d("VPreproc", s);
                    }
                }
                encoder = MediaCodec.createByCodecName(cname);
                encoder.configure(outFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            } catch(IOException e) {
                Log.e("VPreproc", "Codec error - falling back to copy", e);
                return doStreamCopy(extractor, videoTrackIdx, muxer);
            }

            // TODO: Prep the KNN algorithm here

            // run encoding
            boolean done = false, extractorDone = false;
            int inIdx;
            MediaCodec.BufferInfo bufInfo = new MediaCodec.BufferInfo();
            muxer.start();
            encoder.start();
            while(!done) {
                // try to provide input to the decoder if possible
                if(!extractorDone) {
                    while((inIdx = decoder.dequeueInputBuffer(10000)) >= 0) {
                        // fill the buffer
                        ByteBuffer ibuf = decoder.getInputBuffer(inIdx);
                        int length = extractor.readSampleData(ibuf, 0);
                        if (length == -1) {
                            extractorDone = true;
                            Log.d("VPreproc", "Extractor done");
                            decoder.queueInputBuffer(inIdx, 0, 0, -1,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            break;
                        }
                        long ptime = extractor.getSampleTime();
                        extractor.advance();
                        decoder.queueInputBuffer(inIdx, 0, length, ptime, 0);
                    }
                }

                // read a chunk out of the decoder if available
                int outIdx = decoder.dequeueOutputBuffer(bufInfo, 10000);
                if(outIdx >= 0) {
                //if((outIdx >= 0) &&
                       // (bufInfo.presentationTimeUs >= (options.startTime*1000)) &&
                       // (bufInfo.presentationTimeUs < (options.endTime*1000))) {
                    Image img = decoder.getOutputImage(outIdx);

                    // update progress
                    publishProgress(Pair.create(
                            (int)(bufInfo.presentationTimeUs/1000) - options.startTime,
                            (options.endTime - options.startTime)));

                    // process the image
                    // TODO: KNN quantization happens here

                    // load it into the encoder
                    while((inIdx = encoder.dequeueInputBuffer(10000)) == -1) {
                        Log.d("VPreproc", "Dumping data to mux");
                        // move output into muxer
                        MediaCodec.BufferInfo encBufInfo = new MediaCodec.BufferInfo();
                        int encOutIdx = encoder.dequeueOutputBuffer(encBufInfo, -1);
                        if(encOutIdx < 0) continue;
                        ByteBuffer obuf = encoder.getOutputBuffer(encOutIdx);
                        muxer.writeSampleData(videoTrackIdx, obuf, encBufInfo);
                        encoder.releaseOutputBuffer(encOutIdx, false);
                    }

                    // copy the image into the encoder buffer
                    Image encImg = encoder.getInputImage(inIdx);
                    ImageUtil.blit(img, img.getCropRect(), encImg, encImg.getCropRect());
                    encImg.setTimestamp(img.getTimestamp());

                    try {
                        encoder.queueInputBuffer(inIdx, 0, bufInfo.size, -1, 0);
                    } catch(IllegalArgumentException e) {
                        Log.e("VPreproc", "Queue input error", e);
                    }
                    decoder.releaseOutputBuffer(outIdx, false);
                } else if(outIdx >= 0) {
                    Log.d("VPreproc", "Autorelease");
                    decoder.releaseOutputBuffer(outIdx, false);
                } else if(extractorDone) {
                    done = true;
                }
            }

            // close the encoder streams
            while((inIdx = encoder.dequeueInputBuffer(10)) == -1) {
                // move output into muxer
                MediaCodec.BufferInfo encBufInfo = new MediaCodec.BufferInfo();
                int encOutIdx = encoder.dequeueOutputBuffer(encBufInfo, -1);
                ByteBuffer obuf = encoder.getOutputBuffer(encOutIdx);
                muxer.writeSampleData(videoTrackIdx, obuf, encBufInfo);
                encoder.releaseOutputBuffer(encOutIdx, false);
            }
            encoder.queueInputBuffer(inIdx, 0, 0, -1,
                MediaCodec.BUFFER_FLAG_END_OF_STREAM);

            // flush encoder output
            int outIdx;
            MediaCodec.BufferInfo encBufInfo = new MediaCodec.BufferInfo();
            while((outIdx = encoder.dequeueOutputBuffer(encBufInfo, 1000000)) >= 0) {
                ByteBuffer obuf = encoder.getOutputBuffer(outIdx);
                muxer.writeSampleData(videoTrackIdx, obuf, encBufInfo);
                encoder.releaseOutputBuffer(outIdx, false);
            }
            encoder.stop();
            muxer.stop();

            // close everything else
            decoder.stop();

            Log.d("VPreproc", "All done!");
            return null;
            */
        }

        private Void doStreamCopy(MediaExtractor extract, int strmIdx, MediaMuxer muxer) {
            MediaCodec.BufferInfo bufInfo = new MediaCodec.BufferInfo();
            int inIdx, outIdx;
            ByteBuffer buf = ByteBuffer.allocate(8192*1024);
            muxer.start();
            while(true) {
                // try to provide input to the muxer
                int length = extract.readSampleData(buf, 0);
                if(length == -1) break;
                bufInfo.presentationTimeUs = extract.getSampleTime();
                bufInfo.offset = 0;
                bufInfo.size = length;
                bufInfo.flags = 0;
                extract.advance();
                if((bufInfo.presentationTimeUs/1000 >= options.startTime) &&
                        (bufInfo.presentationTimeUs/1000 <= options.endTime)) {
                    bufInfo.presentationTimeUs -= options.startTime*1000;
                    muxer.writeSampleData(strmIdx, buf, bufInfo);
                }
            }
            muxer.stop();
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
