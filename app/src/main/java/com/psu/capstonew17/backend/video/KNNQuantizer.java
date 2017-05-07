package com.psu.capstonew17.backend.video;

import android.graphics.ImageFormat;
import android.media.Image;

import java.util.HashMap;

/**
 * Wrapper class for the KNN RenderScript kernel. Handles color-reducing video frames.
 *
 * TODO: Switch kernels to renderscript - current version runs in JVM
 */
public class KNNQuantizer {
    private int[] colorCounts = new int[256*256*256];
    private int k; // how many colors to quantize to

    public KNNQuantizer(int k) {
        this.k = k;

        for(int i = 0;i < colorCounts.length;i++)
            colorCounts[i] = 0;
    }

    /** Accept an input frame and analyze the colors in the image */
    public void scanFrame(Image f) {
        // iterate through pixels and note their colors
        if(getPlaneCount(f) == 1) return; // only works for 3-channel images
        Image.Plane planes[] = f.getPlanes();

        // for efficiency
        byte[] buf0 = planes[0].getBuffer().array();
        byte[] buf1 = planes[1].getBuffer().array();
        byte[] buf2 = planes[2].getBuffer().array();
        int stride0 = planes[0].getPixelStride();
        int stride1 = planes[1].getPixelStride();
        int stride2 = planes[2].getPixelStride();

        int pixcount = f.getWidth()*f.getHeight();
        for(int i = 0;i < pixcount;i++) {
            byte c0 = buf0[i*stride0], c1 = buf1[i*stride1], c2 = buf2[i*stride2];
            colorCounts[((int)c0 << 16) + ((int)c1 << 8) + (int)c2]++;
        }
    }

    private int getPlaneCount(Image f) {
        switch(f.getFormat()) {
        case ImageFormat.JPEG: return 1;
        case ImageFormat.YUV_420_888: return 3;
        case ImageFormat.YUV_422_888: return 3;
        case ImageFormat.YUV_444_888: return 3;
        case ImageFormat.FLEX_RGB_888: return 3;
        case ImageFormat.FLEX_RGBA_8888: return 3;
        case ImageFormat.RAW_SENSOR: return 1;
        case ImageFormat.RAW_PRIVATE: return 1;
        default: return -1;
        }
    }

    /** Produce means from all scanned colors
     *
     * @param iters The number of iterations to perform
     */
    public void finishAnalysis(int iters) {
    }
}
