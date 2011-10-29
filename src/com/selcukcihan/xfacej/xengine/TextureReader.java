
package com.selcukcihan.android.xface.xengine;


//ANDROID
/*
import com.sun.opengl.util.BufferUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
*/
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Image loading class that converts BufferedImages into a data
 * structure that can be easily passed to OpenGL.
 * @author Pepijn Van Eeckhoudt
 * @author Selcuk Cihan
 */
public class TextureReader {
    public static Texture readTexture(String filename) throws IOException {
        return readTexture(filename, false);
    }

    public static Texture readTexture(String filename, boolean storeAlphaChannel) throws IOException {
        Bitmap bufferedImage;
        if (filename.endsWith(".bmp")) {
            bufferedImage = BitmapLoader.loadBitmap(filename);
        } else {
            bufferedImage = readImage(filename);
        }
        return readPixels(bufferedImage, storeAlphaChannel);
    }

    private static Bitmap readImage(String resourceName) throws IOException {
    	return BitmapFactory.decodeStream(ResourceRetriever.getResourceAsStream(resourceName));
        //return ImageIO.read(ResourceRetriever.getResourceAsStream(resourceName));
    }

    private static Texture readPixels(Bitmap img, boolean storeAlphaChannel) {
    	//img.copyPixelsToBuffer(un)
        int[] packedPixels = new int[img.getWidth() * img.getHeight()];
        img.getPixels(packedPixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        int bytesPerPixel = storeAlphaChannel ? 4 : 3;
        //ByteBuffer unpackedPixels = BufferUtil.newByteBuffer(packedPixels.length * bytesPerPixel);
        ByteBuffer unpackedPixels = ByteBuffer.allocateDirect(packedPixels.length * bytesPerPixel);

        for (int row = img.getHeight() - 1; row >= 0; row--) {
            for (int col = 0; col < img.getWidth(); col++) {
                int packedPixel = packedPixels[row * img.getWidth() + col];
                unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 0) & 0xFF));
                if (storeAlphaChannel) {
                    unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));
                }
            }
        }

        unpackedPixels.flip();


        return new Texture(unpackedPixels, img.getWidth(), img.getHeight());
    }

    public static class Texture {
        private ByteBuffer pixels;
        private int width;
        private int height;

        public Texture(ByteBuffer pixels, int width, int height) {
            this.height = height;
            this.pixels = pixels;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public ByteBuffer getPixels() {
            return pixels;
        }

        public int getWidth() {
            return width;
        }
    }
}