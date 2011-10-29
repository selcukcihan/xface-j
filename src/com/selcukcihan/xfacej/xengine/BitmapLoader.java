
package com.selcukcihan.android.xface.xengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// ANDROID
import java.io.IOException;
import java.io.InputStream;

/**
 * Windows bitmap file loader.
 * @author Selcuk Cihan
 */
public class BitmapLoader {
    public static Bitmap loadBitmap(String file) throws IOException {
    	InputStream input = null;
    	Bitmap image = null;
    	try
    	{
    		input = ResourceRetriever.getResourceAsStream(file);
    		image = BitmapFactory.decodeStream(input);
        }
    	finally
    	{
            try
            {
                if (input != null)
                    input.close();
            }
            catch (IOException e)
            {
            }
        }
    	return image;
    }
}
