package com.psu.capstonew17.backend.video;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;

import java.nio.ByteBuffer;

/**
 * Created by noahz on 6/5/17.
 */

public class ImageUtil {
    // copy pixels from source to destination within the given region
    public static void blit(Image src, Rect srcRect, Image dst, Rect dstRect) {
        if(src.getFormat() != dst.getFormat())
            throw new UnsupportedOperationException("blit() cannot convert formats");
        switch(src.getFormat()) {
        case ImageFormat.YUV_420_888:
            blit_yuv420_888(src, srcRect, dst, dstRect);
            break;
        default:
            throw new UnsupportedOperationException("Unsupported image format in blit()");
        }
    }

    /**
     * Perform blit with YUV420_888 format - this format has one element per pixel for Y, and one
     * element per four-pixel square group for U and V.
     */
    private static void blit_yuv420_888(Image src, Rect srcRect, Image dst, Rect dstRect) {
        Image.Plane srcPlanes[] = src.getPlanes();
        Image.Plane dstPlanes[] = dst.getPlanes();

        for(int i = 0;i < srcPlanes.length;i++) {
            Image.Plane srcPlane = srcPlanes[i], dstPlane = dstPlanes[i];
            ByteBuffer sbuf = srcPlane.getBuffer(), dbuf = dstPlane.getBuffer();

            // from testing, it looks like Android reports the pixel stride as one greater than
            // its real value
            int s_pxstride = srcPlane.getPixelStride(), s_rowstride = srcPlane.getRowStride();
            int d_pxstride = dstPlane.getPixelStride(), d_rowstride = dstPlane.getRowStride();
            for(int x = srcRect.left;x < srcRect.right;x++) {
                for(int y = srcRect.top;y < srcRect.bottom;y++) {
                    int tx = x, ty = y;
                    if(i == 1 || i == 2) { // adjust for pixel format
                        tx /= 2;
                        ty /= 2;
                    }
                    try {
                        byte s = sbuf.get(x*s_pxstride+y*s_rowstride);
                        dbuf.put(
                                (x - srcRect.left + dstRect.left) * d_pxstride +
                                        (y - srcRect.top + dstRect.top) * d_rowstride, s);
                    } catch(IndexOutOfBoundsException e) {
                        return;
                    }
                }
            }
        }
    }

}
