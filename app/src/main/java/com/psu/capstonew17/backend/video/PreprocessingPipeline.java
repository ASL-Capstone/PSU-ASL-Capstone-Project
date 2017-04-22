package com.psu.capstonew17.backend.video;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A pipeline of video processing steps, associated with a frame source and output file.
 */
public class PreprocessingPipeline {
    private File output;
    private MediaRecorder record;

    private File input;
    private MediaExtractor extractor;
    private MediaCodec decoder;
    private MediaFormat inputFormat;
    private ByteBuffer inBuffer;

    public PreprocessingPipeline(File outFile, File inFile) throws IOException {
        output = outFile;
        input = inFile;

        // open the input to figure out the target size
        extractor = new MediaExtractor();
        extractor.setDataSource(input.getPath());
        int videoTrack = -1;
        MediaFormat trackFmt = null;
        for(int i = 0;i < extractor.getTrackCount();i++) {
            trackFmt = extractor.getTrackFormat(i);
            if(trackFmt.getString(MediaFormat.KEY_MIME).startsWith("video")) {
                videoTrack = i;
                extractor.selectTrack(i);
                break;
            }
        }
        if(videoTrack == -1) throw new RuntimeException("No video tracks available");
        int srcWidth = trackFmt.getInteger(MediaFormat.KEY_WIDTH);
        int srcHeight = trackFmt.getInteger(MediaFormat.KEY_HEIGHT);
        decoder = MediaCodec.createDecoderByType(trackFmt.getString(MediaFormat.KEY_MIME));
        inputFormat = trackFmt;

        // figure out target width/height
        int tgtWidth = srcWidth/2,
            tgtHeight = srcHeight/2;

        // find a viable media codec
        record = new MediaRecorder();
        record.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        record.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        record.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        record.setVideoSize(tgtWidth, tgtHeight);
        record.setOutputFile(outFile.getPath());

        record.prepare();
        decoder.setOutputSurface(record.getSurface());
        decoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);

        decoder.setCallback(new MediaCodec.Callback() {
            @Override
            public void onInputBufferAvailable(@NonNull MediaCodec mediaCodec, int i) {
                extractor.readSampleData(decoder.getInputBuffer(i), 0);
                extractor.advance();
            }

            @Override
            public void onOutputBufferAvailable(@NonNull MediaCodec mediaCodec, int i, @NonNull MediaCodec.BufferInfo bufferInfo) {
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    record.stop();
                    extractor.release();
                } else {
                    mediaCodec.releaseOutputBuffer(i, true);
                }
            }

            @Override
            public void onError(@NonNull MediaCodec mediaCodec, @NonNull MediaCodec.CodecException e) {
                record.stop();
                extractor.release();
            }

            @Override
            public void onOutputFormatChanged(@NonNull MediaCodec mediaCodec, @NonNull MediaFormat mediaFormat) {
            }
        });
    }

    public void start() throws IOException {
        record.start();
        decoder.configure();
        decoder.start();
    }
}
