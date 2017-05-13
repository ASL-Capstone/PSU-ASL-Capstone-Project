//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.video;

import android.content.Context;
import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Pair;

import com.psu.capstonew17.backend.api.VideoManager;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A pipeline of video processing steps, associated with a frame source and output file.
 */
public class PreprocessingPipeline {
    private File output;
    private Uri input;
    private VideoManager.ImportOptions options;
    private Context ctx;

    private PreprocessingListener listener = null;

    public interface PreprocessingListener {
        void onCompleted();
        void onFailed();
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

    private class PreprocessingOperation extends AsyncTask<Pair<Uri, File>, Void, Void> {
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

            // figure out target width/height and format
            //int tgtWidth = srcWidth/2, tgtHeight = srcHeight/2;
            int tgtWidth = srcWidth, tgtHeight = srcHeight; // TODO: Resizing
            MediaFormat outFormat = MediaFormat.createVideoFormat("video/h264", tgtWidth, tgtHeight);
            outFormat.setInteger(MediaFormat.KEY_BIT_RATE, 20*1024*1024); // 20 Mbps

            // set up a muxer
            MediaMuxer muxer;
            int videoTrackIdx;
            MediaCodec encoder;
            try {
                encoder = MediaCodec.createByCodecName(
                        new MediaCodecList(MediaCodecList.REGULAR_CODECS)
                                .findEncoderForFormat(outFormat));
                encoder.configure(outFormat, null, null, 0);
                muxer = new MediaMuxer(out.getPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                videoTrackIdx = muxer.addTrack(outFormat);
            } catch(IOException e) {
                cancel(false);
                return null;
            }

            // TODO: Prep the KNN algorithm here

            // run encoding
            boolean done = false, extractorDone = false;
            MediaCodec.BufferInfo bufInfo = new MediaCodec.BufferInfo();
            muxer.start();
            encoder.start();
            decoder.start();
            while(!done) {
                // try to provide input to the decoder if possible
                if(!extractorDone) {
                    int inIdx = decoder.dequeueInputBuffer(10);
                    while (inIdx >= 0) {
                        // fill the buffer
                        ByteBuffer ibuf = decoder.getInputBuffer(inIdx);
                        int length = extractor.readSampleData(ibuf, 0);
                        if (length == -1) {
                            extractorDone = true;
                            decoder.queueInputBuffer(inIdx, 0, 0, -1,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            break;
                        }
                        extractor.advance();
                        decoder.queueInputBuffer(inIdx, 0, length, -1, 0);
                    }
                }

                // read a chunk out of the decoder if available
                int outIdx = decoder.dequeueOutputBuffer(bufInfo, 10);
                if((outIdx >= 0) &&
                        (bufInfo.presentationTimeUs > (options.startTime*1000)) &&
                        (bufInfo.presentationTimeUs < (options.endTime*1000))) {
                    Image img = decoder.getOutputImage(outIdx);

                    // process the image
                    // TODO: KNN quantization happens here

                    // load it into the encoder
                    int inIdx;
                    while((inIdx = encoder.dequeueInputBuffer(10)) == -1) {
                        // move output into muxer
                        MediaCodec.BufferInfo encBufInfo = new MediaCodec.BufferInfo();
                        int encOutIdx = encoder.dequeueOutputBuffer(encBufInfo, -1);
                        ByteBuffer obuf = encoder.getOutputBuffer(encOutIdx);
                        muxer.writeSampleData(videoTrackIdx, obuf, encBufInfo);
                        encoder.releaseOutputBuffer(encOutIdx, false);
                    }

                    // copy the image into the encoder buffer
                    Image encImg = encoder.getInputImage(inIdx);
                    img.getPlanes()[0].getBuffer().put(encImg.getPlanes()[0].getBuffer());
                    img.getPlanes()[1].getBuffer().put(encImg.getPlanes()[1].getBuffer());
                    img.getPlanes()[2].getBuffer().put(encImg.getPlanes()[2].getBuffer());
                    encImg.setTimestamp(img.getTimestamp());

                    encoder.queueInputBuffer(inIdx, 0, bufInfo.size, -1, 0);
                    decoder.releaseOutputBuffer(outIdx, false);
                } else if(extractorDone) {
                    done = true;
                }
            }

            // close the encoder streams
            int inIdx;
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
            while((outIdx = encoder.dequeueOutputBuffer(encBufInfo, -1)) >= 0) {
                ByteBuffer obuf = encoder.getOutputBuffer(outIdx);
                muxer.writeSampleData(videoTrackIdx, obuf, encBufInfo);
                encoder.releaseOutputBuffer(outIdx, false);
            }
            encoder.stop();
            muxer.stop();

            // close everything else
            decoder.stop();

            return null;
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
